package com.atguigu.consult.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 语音识别实体类
 * 用于封装语音识别的数据结构
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoiceRecognition {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 音频数据 (Base64字符串或二进制，视具体存储方式而定)
     */
    private String audioData;

    /**
     * 音频格式 (例如: wav, mp3)
     */
    private String audioFormat;

    /**
     * 识别状态 (0: 待处理, 1: 处理中, 2: 成功, 3: 失败)
     */
    private Integer status;

    /**
     * 识别出的文本结果
     */
    private String recognitionResult;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}