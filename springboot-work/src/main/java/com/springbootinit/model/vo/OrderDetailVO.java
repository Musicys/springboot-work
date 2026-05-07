package com.springbootinit.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderDetailVO {

    private Long id;

    private Long jobId;

    private String jobTitle;

    private List<String> jobCovers;

    private BigDecimal salaryMin;

    private BigDecimal salaryMax;

    private Integer orderStatus;

    private String orderStatusDesc;

    private Integer tradeMode;

    private String tradeModeDesc;

    private Date createdAt;

    private Date startTime;

    private Date endTime;

    private Date completedAt;

    private BigDecimal penaltyAmount;

    private Long applicationId;

    private Long userId;

    private Long merchantId;

    private JobDetailVO jobInfo;

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