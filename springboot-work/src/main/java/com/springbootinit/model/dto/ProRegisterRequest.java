package com.springbootinit.model.dto;

import lombok.Data;

@Data
public class ProRegisterRequest {

    private String username;

    private String password;

    private String companyName;

    private String contactPhone;
}
