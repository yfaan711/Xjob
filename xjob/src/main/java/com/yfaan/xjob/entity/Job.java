package com.yfaan.xjob.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_job")
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
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
     * 职位类型
     */
    private Integer jobType;

    /**
     * 价格
     */
    private Integer price;

    /**
     * 工作介绍
     */
    private String introduce;

    /**
     * 工作时长
     */
    @TableField("work_duration")
    private Integer workDuration;

    /**
     * 工作城市
     */
    @TableField("work_city")
    private String workCity;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}