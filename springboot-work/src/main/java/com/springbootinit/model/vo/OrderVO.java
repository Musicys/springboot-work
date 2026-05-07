package com.springbootinit.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderVO {

    private Long id;

    private Long jobId;

    private String jobTitle;

    private String jobCover;

    private BigDecimal salary;

    private Integer orderStatus;

    private String orderStatusDesc;

    private Integer tradeMode;

    private String tradeModeDesc;

    private Date createdAt;

    private Date startTime;

    private Date endTime;
}