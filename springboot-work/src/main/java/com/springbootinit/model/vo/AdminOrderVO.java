package com.springbootinit.model.vo;

import com.springbootinit.model.domain.OdOrders;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AdminOrderVO {

    private Long id;
    private Long applicationId;
    private Long jobId;
    private Long userId;
    private Long merchantId;
    private Integer tradeMode;
    private Integer orderStatus;
    private Long depositFrozenId;
    private BigDecimal penaltyAmount;
    private Date startTime;
    private Date endTime;
    private Date completedAt;
    private Date createdAt;
    private Integer isDepositRefunded;
    private Integer isSettled;

    private String studentId;
    private String realName;
    private Integer age;
    private Integer gender;
    private String phone;
    private String profession;
    private String introduction;
    private String tagName;
    private Integer creditScore;

    private String companyName;
    private String licenseUrl;
    private String contactPhone;
    private String location;
    private String legalPerson;
    private BigDecimal registeredCapital;
    private String companyAddress;
    private String companyIntro;

    public static AdminOrderVO fromOrder(OdOrders order) {
        AdminOrderVO vo = new AdminOrderVO();
        vo.setId(order.getId());
        vo.setApplicationId(order.getApplicationId());
        vo.setJobId(order.getJobId());
        vo.setUserId(order.getUserId());
        vo.setMerchantId(order.getMerchantId());
        vo.setTradeMode(order.getTradeMode());
        vo.setOrderStatus(order.getOrderStatus());
        vo.setDepositFrozenId(order.getDepositFrozenId());
        vo.setPenaltyAmount(order.getPenaltyAmount());
        vo.setStartTime(order.getStartTime());
        vo.setEndTime(order.getEndTime());
        vo.setCompletedAt(order.getCompletedAt());
        vo.setCreatedAt(order.getCreatedAt());
        vo.setIsDepositRefunded(order.getIsDepositRefunded());
        vo.setIsSettled(order.getIsSettled());
        return vo;
    }
}