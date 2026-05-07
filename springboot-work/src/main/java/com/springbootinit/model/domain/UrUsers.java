package com.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户基础表
 * @TableName ur_users
 */
@Data
@TableName(value ="ur_users")
public class UrUsers implements Serializable {
    /**
     * 用户全局唯一 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 1:求职者(C), 2:商家(B), 3:管理员(Admin)
     */
    @TableField("user_type")
    private Integer userType;

    /**
     * 用户名/手机号
     */
    private String username;

    /**
     * 加密密码
     */
    @TableField("password_hash")
    private String passwordHash;

    /**
     * 头像
     */
    @TableField("avatar_url")
    private String avatarUrl;

    /**
     * 所在地区代码
     */
    @TableField("region_code")
    private String regionCode;

    /**
     * 1:正常, 0:封禁, -1:注销
     */
    private Integer status;

    /**
     *
     */
    @TableField("created_at")
    private Date createdAt;

    /**
     *
     */
    @TableField("updated_at")
    private Date updatedAt;


    /**
     * 用户权限
     */
    @TableField("user_rote")
    private Integer userRote;


    /**
     * 信誉分
     */
    @TableField("credit_code") // 告诉MyBatis-Plus这个属性对应数据库的 credit_code 字段
    private Integer creditScore;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
