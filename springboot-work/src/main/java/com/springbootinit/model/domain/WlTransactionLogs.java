package com.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易流水表
 * @TableName wl_transaction_logs
 */
@TableName(value ="wl_transaction_logs")
public class WlTransactionLogs implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long userId;

    /**
     * DEPOSIT_IN, SALARY_PAY, PENALTY_OUT, REFUND
     */
    private String type;

    /**
     * 
     */
    private BigDecimal amount;

    /**
     * 
     */
    private BigDecimal balanceBefore;

    /**
     * 
     */
    private BigDecimal balanceAfter;

    /**
     * 
     */
    private Long relatedOrderId;

    /**
     * 
     */
    private Long relatedFreezeId;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private Date createdAt;

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
     * DEPOSIT_IN, SALARY_PAY, PENALTY_OUT, REFUND
     */
    public String getType() {
        return type;
    }

    /**
     * DEPOSIT_IN, SALARY_PAY, PENALTY_OUT, REFUND
     */
    public void setType(String type) {
        this.type = type;
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
     * 
     */
    public BigDecimal getBalanceBefore() {
        return balanceBefore;
    }

    /**
     * 
     */
    public void setBalanceBefore(BigDecimal balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    /**
     * 
     */
    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    /**
     * 
     */
    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    /**
     * 
     */
    public Long getRelatedOrderId() {
        return relatedOrderId;
    }

    /**
     * 
     */
    public void setRelatedOrderId(Long relatedOrderId) {
        this.relatedOrderId = relatedOrderId;
    }

    /**
     * 
     */
    public Long getRelatedFreezeId() {
        return relatedFreezeId;
    }

    /**
     * 
     */
    public void setRelatedFreezeId(Long relatedFreezeId) {
        this.relatedFreezeId = relatedFreezeId;
    }

    /**
     * 
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     */
    public void setDescription(String description) {
        this.description = description;
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
        WlTransactionLogs other = (WlTransactionLogs) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
            && (this.getBalanceBefore() == null ? other.getBalanceBefore() == null : this.getBalanceBefore().equals(other.getBalanceBefore()))
            && (this.getBalanceAfter() == null ? other.getBalanceAfter() == null : this.getBalanceAfter().equals(other.getBalanceAfter()))
            && (this.getRelatedOrderId() == null ? other.getRelatedOrderId() == null : this.getRelatedOrderId().equals(other.getRelatedOrderId()))
            && (this.getRelatedFreezeId() == null ? other.getRelatedFreezeId() == null : this.getRelatedFreezeId().equals(other.getRelatedFreezeId()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getBalanceBefore() == null) ? 0 : getBalanceBefore().hashCode());
        result = prime * result + ((getBalanceAfter() == null) ? 0 : getBalanceAfter().hashCode());
        result = prime * result + ((getRelatedOrderId() == null) ? 0 : getRelatedOrderId().hashCode());
        result = prime * result + ((getRelatedFreezeId() == null) ? 0 : getRelatedFreezeId().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
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
        sb.append(", type=").append(type);
        sb.append(", amount=").append(amount);
        sb.append(", balanceBefore=").append(balanceBefore);
        sb.append(", balanceAfter=").append(balanceAfter);
        sb.append(", relatedOrderId=").append(relatedOrderId);
        sb.append(", relatedFreezeId=").append(relatedFreezeId);
        sb.append(", description=").append(description);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}