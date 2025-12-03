package com.yfaan.xjob.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class JobWithUserDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户id
     */
    private Integer userId; // 确保使用Integer类型与数据库保持一致

    /**
     * 职位名称
     */
    private String job;

    /**
     * 技能要求
     */
    private String skill;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 工作介绍
     */
    private String introduce;

    /**
     * 工作时长
     */
    private String workDuration;

    /**
     * 工作城市
     */
    private String workCity;

    /**
     * 职位类型
     */
    private Integer jobType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String icon;
}