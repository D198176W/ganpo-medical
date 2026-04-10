<<<<<<< HEAD
package com.atguigu.javaailangchain4j.tools;
=======
package com.atguigu.consult.tools;
>>>>>>> 9b1bb41fd6d89941f901e638a4766caca9f2260d


//import com.atguigu.javaailangchain4j.entity.Appointment;
import com.atguigu.consult.entity.Appointment;
//import com.atguigu.javaailangchain4j.service.AppointmentService;
import com.atguigu.consult.service.AppointmentService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppoinmentTools {

    @Autowired
    private AppointmentService appointmentService;

//    查找数据库中是否包含对应的预约记录

    @Tool(name = "预约挂号",value = "根据参数，先执行工具方法queryDepartment查询是否可以预约，给用户回答是否可以预约，让用户确认预约信息，确认后再预约，如果用户没有提到具体的医生姓名，请从向量存储中找到一位医生")

    public String bookAppointment(Appointment appointment) {

        Appointment appointmentDB = appointmentService.getOne(appointment);
        if (appointmentDB == null) {
//            防止大模型幻觉设置了Id
            appointment.setId(null);
            if (appointmentService.save(appointment)) {
                return "预约成功";
            } else {
                return "预约失败";
            }
        }
        return "你在相同的科室和时间有预约";

    }
    @Tool(name = "取消预约挂号",value ="根据参数，查询预约是否存在，如果存在则取消预约并返回取消预约成功，否则返回取消预约失败" )
            public String cancelAppointment(Appointment appointment){
                Appointment appointmentDB = appointmentService.getOne(appointment);
                if(appointmentDB != null){
                    if(appointmentService.removeById(appointmentDB.getId())){
                        return "取消预约成功";
                    }
                    else{
                        return "取消预约失败";
                    }
                }
                return "取消失败，你没有预约记录";
            }

    @Tool(name = "查询是否有号源",value ="根据科室，医生，时间查询是否有号源，并返回给用户")
    public boolean queryDepartment(
            @P(value = "科室名称") String name,
            @P(value = "日期") String date,
            @P(value = "时间，可选上午，下午") String time,
            @P(value = "医生名称",required = false) String doctorName


    ){
        System.out.println("查询是否有号源");
        System.out.println("科室名称：" + name);
        System.out.println("日期：" + date);
        System.out.println("时间：" + time);
        System.out.println("医生名称：" + doctorName);

        return true;

    }



}
