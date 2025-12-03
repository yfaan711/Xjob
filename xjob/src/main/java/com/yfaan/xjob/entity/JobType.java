package com.yfaan.xjob.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
//@Data: 自动生成getter、setter、toString、equals和hashCode方法 来自lombook
@EqualsAndHashCode(callSuper = false)
//@EqualsAndHashCode: 自动生成equals和hashCode方法 来自lombook
@Accessors(chain = true)
//链式编程
@TableName("tb_job_type")
//指定实体类对应的数据库表名，来自MybatisPlus

//Serializable: 序列化接口，用于对象序列化，实现对象序列化，将对象转换成字节数组，以便存储或传输。
public class JobType implements Serializable {
    //序列化版本号
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    //@TableId: 指定主键字段，来自MybatisPlus
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 职位类型名称
     */
    private String name;
    /**
     * 顺序
     */
    private Integer sort;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
