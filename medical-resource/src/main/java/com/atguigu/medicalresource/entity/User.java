package com.atguigu.medicalresource.entity;

import com.baomidou.mybatisplus.annotation.TableName;
<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

=======
import lombok.Data;
>>>>>>> 9b1bb41fd6d89941f901e638a4766caca9f2260d
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
<<<<<<< HEAD
    //@TableId(type = IdType.AUTO)
=======
>>>>>>> 9b1bb41fd6d89941f901e638a4766caca9f2260d
    private Long id;
    private String username;
    private String password; // 已加密（bcrypt）
    private String name;
    private String phone;
<<<<<<< HEAD
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
=======
>>>>>>> 9b1bb41fd6d89941f901e638a4766caca9f2260d
    private LocalDateTime createdAt;
}