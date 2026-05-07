package com.springbootinit.model.vo;

import com.springbootinit.model.domain.WlWallets;
import lombok.Data;

@Data
public class LoginVO {

    private Long userId;

    private String username;

    private String token;

    private Integer userType;

    private String avatarUrl;

    private Integer userRote;

    private String studentId;

    private String realName;

    private Integer age;

    private Integer gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 专业
     */
    private String profession;

    /**
     * 简历
     */
    private String introduction;

    /**
     * 信用分
     */
    private Integer creditScore;
    /**
     * 标签名称
     *  */
    private String tagName;

    /**
     * 钱包金额
     */
    private WlWallets wlWallets;



}
