package com.springbootinit.model.vo;

import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class LoginVO {

    private Long userId;

    private String username;

    private String token;

    private Integer userType;

    private String avatarUrl;

    private Integer userRote;
}
