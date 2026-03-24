package com.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 信誉分流水表
 * @TableName ct_credit_logs
 */
@TableName(value ="ct_credit_logs")
public class CtCreditLogs implements Serializable {
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
     * +5 或 -5
     */
    private Integer changeAmount;

    /**
     * COMPLETE_JOB, NO_SHOW, ESCAPE, MANUAL_ADJUST
     */
    private String reasonType;

    /**
     * 关联订单 ID
     */
    private Long relatedOrderId;

    /**
     * 操作人 ID (系统或管理员)
     */
    private Long operatorId;

    /**
     * 
     */
    private String description;

    /**
     * 变动后的分数
     */
    private Integer balanceAfter;

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
     * +5 或 -5
     */
    public Integer getChangeAmount() {
        return changeAmount;
    }

    /**
     * +5 或 -5
     */
    public void setChangeAmount(Integer changeAmount) {
        this.changeAmount = changeAmount;
    }

    /**
     * COMPLETE_JOB, NO_SHOW, ESCAPE, MANUAL_ADJUST
     */
    public String getReasonType() {
        return reasonType;
    }

    /**
     * COMPLETE_JOB, NO_SHOW, ESCAPE, MANUAL_ADJUST
     */
    public void setReasonType(String reasonType) {
        this.reasonType = reasonType;
    }

    /**
     * 关联订单 ID
     */
    public Long getRelatedOrderId() {
        return relatedOrderId;
    }

    /**
     * 关联订单 ID
     */
    public void setRelatedOrderId(Long relatedOrderId) {
        this.relatedOrderId = relatedOrderId;
    }

    /**
     * 操作人 ID (系统或管理员)
     */
    public Long getOperatorId() {
        return operatorId;
    }

    /**
     * 操作人 ID (系统或管理员)
     */
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
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
     * 变动后的分数
     */
    public Integer getBalanceAfter() {
        return balanceAfter;
    }

    /**
     * 变动后的分数
     */
    public void setBalanceAfter(Integer balanceAfter) {
        this.balanceAfter = balanceAfter;
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
        CtCreditLogs other = (CtCreditLogs) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getChangeAmount() == null ? other.getChangeAmount() == null : this.getChangeAmount().equals(other.getChangeAmount()))
            && (this.getReasonType() == null ? other.getReasonType() == null : this.getReasonType().equals(other.getReasonType()))
            && (this.getRelatedOrderId() == null ? other.getRelatedOrderId() == null : this.getRelatedOrderId().equals(other.getRelatedOrderId()))
            && (this.getOperatorId() == null ? other.getOperatorId() == null : this.getOperatorId().equals(other.getOperatorId()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getBalanceAfter() == null ? other.getBalanceAfter() == null : this.getBalanceAfter().equals(other.getBalanceAfter()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getChangeAmount() == null) ? 0 : getChangeAmount().hashCode());
        result = prime * result + ((getReasonType() == null) ? 0 : getReasonType().hashCode());
        result = prime * result + ((getRelatedOrderId() == null) ? 0 : getRelatedOrderId().hashCode());
        result = prime * result + ((getOperatorId() == null) ? 0 : getOperatorId().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getBalanceAfter() == null) ? 0 : getBalanceAfter().hashCode());
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
        sb.append(", changeAmount=").append(changeAmount);
        sb.append(", reasonType=").append(reasonType);
        sb.append(", relatedOrderId=").append(relatedOrderId);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", description=").append(description);
        sb.append(", balanceAfter=").append(balanceAfter);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}