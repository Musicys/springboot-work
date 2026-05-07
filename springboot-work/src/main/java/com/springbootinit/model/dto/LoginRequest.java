package com.springbootinit.model.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;

    private String password;
    // 学号
    private String studentId;
    // 年龄
    private Integer age;
    // 性别
    private Integer gender;
    // 真实姓名
    private String realName;
    // 电话
    private String phone;
    // 职业
    private String profession;

    // 个人简介
    private String introduction;

    // 头像URL
    private String avatarUrl;
    //信誉分
    private Integer creditScore;
}
