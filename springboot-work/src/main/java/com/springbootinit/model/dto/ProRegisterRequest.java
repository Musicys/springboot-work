package com.springbootinit.model.dto;

import lombok.Data;

@Data
public class ProRegisterRequest {

    private String username;

    private String password;

    private String companyName;

    private String contactPhone;

    private String location;

    private String legalPerson;

    private String legalIdCard;

    private String companyImages;

    private String registeredCapital;

    private String companyAddress;

    private String companyIntro;

    private String avatarUrl;

    private String licenseUrl;
}
