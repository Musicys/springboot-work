package com.springbootinit.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class JobVO {

    private Long id;

    private String coverImage;

    private String title;

    private BigDecimal salaryMin;

    private BigDecimal salaryMax;

    private Integer jobType;

    private String jobTypeDesc;

    private String regionName;

    private Integer workTimeType;

    private String workTimeDesc;

    private String briefIntro;

    private Integer recruitNum;

    private Date expireTime;

    private Date publishTime;

    private Integer status;
}