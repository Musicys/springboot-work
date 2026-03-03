package com.yupi.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 虚拟交易流水表
 * @TableName t_transaction_log
 */
@TableName(value ="t_transaction_log")
public class TTransactionLog implements Serializable {
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
     * 关联订单ID
     */
    private Long orderId;

    /**
     * 类型: 1-充值, 2-支付押金, 3-退还押金, 4-结算收入, 5-退款
     */
    private Integer type;

    /**
     * 变动金额
     */
    private BigDecimal amount;

    /**
     * 变动后余额
     */
    private BigDecimal balanceAfter;

    /**
     *
     */
    private String description;

    /**
     *
     */
    private Date createTime;

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
     * 关联订单ID
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 关联订单ID
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 类型: 1-充值, 2-支付押金, 3-退还押金, 4-结算收入, 5-退款
     */
    public Integer getType() {
        return type;
    }

    /**
     * 类型: 1-充值, 2-支付押金, 3-退还押金, 4-结算收入, 5-退款
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 变动金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 变动金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 变动后余额
     */
    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    /**
     * 变动后余额
     */
    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
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
    public Date getCreateTime() {
        return createTime;
    }

    /**
     *
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
        TTransactionLog other = (TTransactionLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
            && (this.getBalanceAfter() == null ? other.getBalanceAfter() == null : this.getBalanceAfter().equals(other.getBalanceAfter()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getBalanceAfter() == null) ? 0 : getBalanceAfter().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
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
        sb.append(", type=").append(type);
        sb.append(", amount=").append(amount);
        sb.append(", balanceAfter=").append(balanceAfter);
        sb.append(", description=").append(description);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
