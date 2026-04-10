package com.atguigu.healthmanagement.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//@Component
//public class MetaObjectHandlerConfig implements MetaObjectHandler {
//
//    @Override
//    public void insertFill(MetaObject metaObject) {
//        this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, LocalDateTime.now());
//        this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
//    }
//}

@Component
public class MetaObjectHandlerConfig implements MetaObjectHandler {

    /**
     * 插入时自动填充字段
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 创建时间
        this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, LocalDateTime.now());

        // 更新时间（插入时也设置）
        this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());

        // 逻辑删除字段默认值（如果存在）
        if (metaObject.hasSetter("deleted")) {
            this.strictInsertFill(metaObject, "deleted", Integer.class, 0);
        }
    }

    /**
     * 更新时自动填充字段
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间
        this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
    }
}