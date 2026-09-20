package com.atguigu.consult.tools;

import com.atguigu.consult.entity.Appointment;
import com.atguigu.consult.security.SensitiveDataEncryptor;
import com.atguigu.consult.service.AppointmentService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppointmentTools {

    private static final Logger log = LoggerFactory.getLogger(AppointmentTools.class);

    /** 每个科室每个半天时段的号源容量 */
    private static final int SLOT_CAPACITY_PER_SESSION = 30;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private SensitiveDataEncryptor sensitiveDataEncryptor;

    @Tool(name = "预约挂号", value = "根据参数，先执行工具方法queryDepartment查询是否可以预约，给用户回答是否可以预约，让用户确认预约信息，确认后再预约，如果用户没有提到具体的医生姓名，请从向量存储中找到一位医生")
    public String bookAppointment(Appointment appointment) {

        // 身份证号确定性加密后落库（支持等值查询，明文不持久化）
        appointment.setIdCard(sensitiveDataEncryptor.encryptDeterministic(appointment.getIdCard()));

        Appointment appointmentDB = appointmentService.getOne(appointment);
        if (appointmentDB == null) {
            // 防止大模型幻觉设置了Id
            appointment.setId(null);
            if (appointmentService.save(appointment)) {
                return "预约成功";
            } else {
                return "预约失败";
            }
        }
        return "你在相同的科室和时间有预约";

    }

    @Tool(name = "取消预约挂号", value = "根据参数，查询预约是否存在，如果存在则取消预约并返回取消预约成功，否则返回取消预约失败")
    public String cancelAppointment(Appointment appointment) {
        // 与存储侧保持一致的加密口径
        appointment.setIdCard(sensitiveDataEncryptor.encryptDeterministic(appointment.getIdCard()));
        Appointment appointmentDB = appointmentService.getOne(appointment);
        if (appointmentDB != null) {
            if (appointmentService.removeById(appointmentDB.getId())) {
                return "取消预约成功";
            } else {
                return "取消预约失败";
            }
        }
        return "取消失败，你没有预约记录";
    }

    @Tool(name = "查询是否有号源", value = "根据科室，医生，时间查询是否有号源，并返回给用户")
    public String queryDepartment(
            @P(value = "科室名称") String name,
            @P(value = "日期") String date,
            @P(value = "时间，可选上午，下午") String time,
            @P(value = "医生名称", required = false) String doctorName
    ) {
        log.info("查询号源: 科室={}, 日期={}, 时段={}, 医生={}", name, date, time, doctorName);

        long occupied = appointmentService.countOccupiedSlots(name, date, time, doctorName);
        long remaining = SLOT_CAPACITY_PER_SESSION - occupied;

        if (remaining <= 0) {
            return String.format("抱歉，%s %s %s 的号源已约满（容量%d，已约%d），建议更换日期或时段",
                    name, date, time, SLOT_CAPACITY_PER_SESSION, occupied);
        }
        return String.format("%s %s %s 有号源，剩余 %d 个（容量 %d，已约 %d），可以预约",
                name, date, time, remaining, SLOT_CAPACITY_PER_SESSION, occupied);
    }

}
