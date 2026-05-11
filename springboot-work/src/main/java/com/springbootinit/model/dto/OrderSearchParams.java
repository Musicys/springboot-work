package com.springbootinit.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 订单搜索参数类
 */
@ApiModel(description = "订单搜索参数类")
public class OrderSearchParams {

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "商家ID")
    private Long merchantId;

    @ApiModelProperty(value = "兼职ID")
    private Long jobId;

    @ApiModelProperty(value = "订单状态：1:待入职，2:进行中(押金冻结), 3:已完成，4:纠纷中，5:已结款，6:用户爽约，7:异常终止")
    private Integer orderStatus;

    @ApiModelProperty(value = "交易模式：1:平台担保，2:线下交易")
    private Integer tradeMode;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getTradeMode() {
        return tradeMode;
    }

    public void setTradeMode(Integer tradeMode) {
        this.tradeMode = tradeMode;
    }
}
