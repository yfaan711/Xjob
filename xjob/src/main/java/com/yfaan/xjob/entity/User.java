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
/*自动生成实体类的getter、setter、toString、equals、hashCode方法，以及无参构造器。*/
@EqualsAndHashCode(callSuper = false)
/*生成的equals和hashCode方法不包含父类的字段（仅比较当前类的字段）*/
@Accessors(chain = true)
/*链式调用*/
@TableName("tb_user")
/*Mybatis-plus注解，将实体类与数据库表映射*/
public class User implements Serializable {
    /*Serializable: Java 的序列化接口，实现该接口的类可以将对象转换为字节流（序列化），并在需要时重新恢复为对象（反序列化）*/
    private static final long serialVersionUID = 1L;//给序列化对象加一个 “固定身份牌”，避免因类结构微小变化导致反序列化崩溃


    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 密码，加密存储
     */
    private String password;

    /**
     * 角色 personal-普通用户  provider-服务提供者
     */
    private String role;

    /**
     * 昵称，默认是随机字符
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String icon = "";

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
