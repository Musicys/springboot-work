package com.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金冻结记录表
 * @TableName wl_freeze_records
 */
@TableName(value ="wl_freeze_records")
public class WlFreezeRecords implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 被冻结资金的用户 (通常是商家押金，但也可能是用户押金)
     */
    private Long userId;

    /**
     * 
     */
    private Long orderId;

    /**
     * 
     */
    private BigDecimal amount;

    /**
     * 1:冻结中，2:已解冻(退还), 3:已扣除(赔付)
     */
    private Integer status;

    /**
     * 
     */
    private String releaseReason;

    /**
     * 
     */
    private Date createdAt;

    /**
     * 
     */
    private Date resolvedAt;

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
     * 被冻结资金的用户 (通常是商家押金，但也可能是用户押金)
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 被冻结资金的用户 (通常是商家押金，但也可能是用户押金)
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 1:冻结中，2:已解冻(退还), 3:已扣除(赔付)
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 1:冻结中，2:已解冻(退还), 3:已扣除(赔付)
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 
     */
    public String getReleaseReason() {
        return releaseReason;
    }

    /**
     * 
     */
    public void setReleaseReason(String releaseReason) {
        this.releaseReason = releaseReason;
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
     * 
     */
    public Date getResolvedAt() {
        return resolvedAt;
    }

    /**
     * 
     */
    public void setResolvedAt(Date resolvedAt) {
        this.resolvedAt = resolvedAt;
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
        WlFreezeRecords other = (WlFreezeRecords) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getReleaseReason() == null ? other.getReleaseReason() == null : this.getReleaseReason().equals(other.getReleaseReason()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getResolvedAt() == null ? other.getResolvedAt() == null : this.getResolvedAt().equals(other.getResolvedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getReleaseReason() == null) ? 0 : getReleaseReason().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getResolvedAt() == null) ? 0 : getResolvedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", orderId=").append(orderId);
        sb.append(", amount=").append(amount);
        sb.append(", status=").append(status);
        sb.append(", releaseReason=").append(releaseReason);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", resolvedAt=").append(resolvedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}