package com.springbootinit.model.vo;

import com.springbootinit.model.domain.OdOrders;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderVO {
    // 订单信息
    private Long id;
    private Long applicationId;
    private Long jobId;
    private Long userId;
    private Long merchantId;
    private Integer tradeMode;
    private String tradeModeDesc;
    private Integer orderStatus;
    private String orderStatusDesc;
    private Long depositFrozenId;
    private BigDecimal penaltyAmount;
    private Date startTime;
    private Date endTime;
    private Date completedAt;
    private Date createdAt;
    private Integer isDepositRefunded;
    private Integer isSettled;

    // 用户信息
    private String studentId;
    private String realName;
    private Integer age;
    private Integer gender;
    private String phone;
    private String profession;
    private String introduction;
    private String tagName;


    // 兼职岗位信息
    private String jobTitle;
    private String jobCover;
    private BigDecimal salary;
    private BigDecimal depositAmount;
    private String jobBriefIntro;
    private BigDecimal jobSalaryMin;
    private BigDecimal jobSalaryMax;
    private String jobRegionName;
    private Date expireTime;

    // 商家信息
    private String companyName;
    private String contactPhone;

    public static OrderVO fromOrder(OdOrders order) {
        OrderVO vo = new OrderVO();
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
