package com.springbootinit.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class JobDetailVO {

    private Long id;

    private List<String> coverImages;

    private BigDecimal salaryMin;

    private BigDecimal salaryMax;

    private Integer jobType;

    private String jobTypeDesc;

    private String regionName;

    private Integer recruitNum;

    private Long remainingValidDays;

    private Date publishTime;

    private String description;

    private String jobDescCover;

    private String jobDetail;

    private Integer settlementCycle;

    private String settlementCycleDesc;

    private MerchantInfo merchantInfo;

    @Data
    public static class MerchantInfo {
        private Long id;
        private String merchantName;
        private String businessLicense;
        private String contactPhone;
        private String address;
    }
}