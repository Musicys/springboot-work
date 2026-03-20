package com.yupi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @TableName app_user
 */
@Data
@TableName(value ="app_user")
public class AppUser implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码（明文）
     */
    private String password;


    private  String url;

    private  String name;

    /**
     * 钱包余额
     */
    private BigDecimal wallet;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
