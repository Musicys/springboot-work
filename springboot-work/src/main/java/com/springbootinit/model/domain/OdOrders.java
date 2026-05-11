package com.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单主表
 * @TableName od_orders
 */
@TableName(value ="od_orders")
public class OdOrders implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long applicationId;

    /**
     * 
     */
    private Long jobId;

    /**
     * 
     */
    private Long userId;

    /**
     * 
     */
    private Long merchantId;

    /**
     * 1:平台担保，2:线下交易
     */
    private Integer tradeMode;

    /**
     * 1:待入职，2:进行中(押金冻结), 3:已完成，4:纠纷中，5:已结款，6:用户爽约，7:异常终止
     */
    private Integer orderStatus;

    /**
     * 关联 wl_freeze_records.id
     */
    private Long depositFrozenId;

    /**
     * 违约金金额
     */
    private BigDecimal penaltyAmount;

    /**
     * 
     */
    private Date startTime;

    /**
     * 
     */
    private Date endTime;

    /**
     * 
     */
    private Date completedAt;

    /**
     * 
     */
    private Date createdAt;

    /**
     * 是否退押: 0:false, 1:true
     */
    private Integer isDepositRefunded;

    /**
     * 是否结款: 0:false, 1:true
     */
    private Integer isSettled;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     */
    public Long getApplicationId() {
        return applicationId;
    }

    /**
     * 
     */
    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * 
     */
    public Long getJobId() {
        return jobId;
    }

    /**
     * 
     */
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    /**
     * 
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 
     */
    public Long getMerchantId() {
        return merchantId;
    }

    /**
     * 
     */
    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    /**
     * 1:平台担保，2:线下交易
     */
    public Integer getTradeMode() {
        return tradeMode;
    }

    /**
     * 1:平台担保，2:线下交易
     */
    public void setTradeMode(Integer tradeMode) {
        this.tradeMode = tradeMode;
    }

    /**
     * 1:待入职，2:进行中(押金冻结), 3:已完成，4:纠纷中，5:已结款，6:用户爽约，7:异常终止
     */
    public Integer getOrderStatus() {
        return orderStatus;
    }

    /**
     * 1:待入职，2:进行中(押金冻结), 3:已完成，4:纠纷中，5:已结款，6:用户爽约，7:异常终止
     */
    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * 关联 wl_freeze_records.id
     */
    public Long getDepositFrozenId() {
        return depositFrozenId;
    }

    /**
     * 关联 wl_freeze_records.id
     */
    public void setDepositFrozenId(Long depositFrozenId) {
        this.depositFrozenId = depositFrozenId;
    }

    /**
     * 违约金金额
     */
    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    /**
     * 违约金金额
     */
    public void setPenaltyAmount(BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    /**
     * 
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 
     */
    public Date getCompletedAt() {
        return completedAt;
    }

    /**
     * 
     */
    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    /**
     * 
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 是否退押: 0:false, 1:true
     */
    public Integer getIsDepositRefunded() {
        return isDepositRefunded;
    }

    /**
     * 是否退押: 0:false, 1:true
     */
    public void setIsDepositRefunded(Integer isDepositRefunded) {
        this.isDepositRefunded = isDepositRefunded;
    }

    /**
     * 是否结款: 0:false, 1:true
     */
    public Integer getIsSettled() {
        return isSettled;
    }

    /**
     * 是否结款: 0:false, 1:true
     */
    public void setIsSettled(Integer isSettled) {
        this.isSettled = isSettled;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        OdOrders other = (OdOrders) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getApplicationId() == null ? other.getApplicationId() == null : this.getApplicationId().equals(other.getApplicationId()))
            && (this.getJobId() == null ? other.getJobId() == null : this.getJobId().equals(other.getJobId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getMerchantId() == null ? other.getMerchantId() == null : this.getMerchantId().equals(other.getMerchantId()))
            && (this.getTradeMode() == null ? other.getTradeMode() == null : this.getTradeMode().equals(other.getTradeMode()))
            && (this.getOrderStatus() == null ? other.getOrderStatus() == null : this.getOrderStatus().equals(other.getOrderStatus()))
            && (this.getDepositFrozenId() == null ? other.getDepositFrozenId() == null : this.getDepositFrozenId().equals(other.getDepositFrozenId()))
            && (this.getPenaltyAmount() == null ? other.getPenaltyAmount() == null : this.getPenaltyAmount().equals(other.getPenaltyAmount()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getCompletedAt() == null ? other.getCompletedAt() == null : this.getCompletedAt().equals(other.getCompletedAt()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getIsDepositRefunded() == null ? other.getIsDepositRefunded() == null : this.getIsDepositRefunded().equals(other.getIsDepositRefunded()))
            && (this.getIsSettled() == null ? other.getIsSettled() == null : this.getIsSettled().equals(other.getIsSettled()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getApplicationId() == null) ? 0 : getApplicationId().hashCode());
        result = prime * result + ((getJobId() == null) ? 0 : getJobId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getMerchantId() == null) ? 0 : getMerchantId().hashCode());
        result = prime * result + ((getTradeMode() == null) ? 0 : getTradeMode().hashCode());
        result = prime * result + ((getOrderStatus() == null) ? 0 : getOrderStatus().hashCode());
        result = prime * result + ((getDepositFrozenId() == null) ? 0 : getDepositFrozenId().hashCode());
        result = prime * result + ((getPenaltyAmount() == null) ? 0 : getPenaltyAmount().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getCompletedAt() == null) ? 0 : getCompletedAt().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getIsDepositRefunded() == null) ? 0 : getIsDepositRefunded().hashCode());
        result = prime * result + ((getIsSettled() == null) ? 0 : getIsSettled().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", applicationId=").append(applicationId);
        sb.append(", jobId=").append(jobId);
        sb.append(", userId=").append(userId);
        sb.append(", merchantId=").append(merchantId);
        sb.append(", tradeMode=").append(tradeMode);
        sb.append(", orderStatus=").append(orderStatus);
        sb.append(", depositFrozenId=").append(depositFrozenId);
        sb.append(", penaltyAmount=").append(penaltyAmount);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", completedAt=").append(completedAt);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", isDepositRefunded=").append(isDepositRefunded);
        sb.append(", isSettled=").append(isSettled);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}