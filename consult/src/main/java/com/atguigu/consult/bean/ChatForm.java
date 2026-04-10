package com.atguigu.consult.bean;

import lombok.Data;

@Data
public class ChatForm {
    private Long memoryId;
//    private String memoryId;

    private String message;

    private Boolean isHistoryQuery;

}
