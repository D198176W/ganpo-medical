package com.atguigu.pathology.dto;

import lombok.Data;

@Data
public class ChatRequest {
<<<<<<< HEAD
    private Long userId;
    private String role; // doctor | patient
    private String query;
    private Long sessionId; // optional
=======
    private String userId;
    private String role; // doctor | patient
    private String query;
    private String sessionId; // optional
>>>>>>> 9b1bb41fd6d89941f901e638a4766caca9f2260d
}
