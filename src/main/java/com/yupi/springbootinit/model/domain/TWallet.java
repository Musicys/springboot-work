package com.yupi.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户虚拟钱包表
 * @TableName t_wallet
 */
@TableName(value ="t_wallet")
public class TWallet implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 可用余额
     */
    private BigDecimal balance;

    /**
     * 冻结余额 (押金/交易中)
     */
    private BigDecimal frozenBalance;

    /**
     * 累计充值
     */
    private BigDecimal totalRecharge;

    /**
     * 累计收入
     */
    private BigDecimal totalIncome;

    /**
     *
     */
    private Date updateTime;

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
     * 用户ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 可用余额
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * 可用余额
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * 冻结余额 (押金/交易中)
     */
    public BigDecimal getFrozenBalance() {
        return frozenBalance;
    }

    /**
     * 冻结余额 (押金/交易中)
     */
    public void setFrozenBalance(BigDecimal frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    /**
     * 累计充值
     */
    public BigDecimal getTotalRecharge() {
        return totalRecharge;
    }

    /**
     * 累计充值
     */
    public void setTotalRecharge(BigDecimal totalRecharge) {
        this.totalRecharge = totalRecharge;
    }

    /**
     * 累计收入
     */
    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    /**
     * 累计收入
     */
    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    /**
     *
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     *
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
        TWallet other = (TWallet) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getBalance() == null ? other.getBalance() == null : this.getBalance().equals(other.getBalance()))
            && (this.getFrozenBalance() == null ? other.getFrozenBalance() == null : this.getFrozenBalance().equals(other.getFrozenBalance()))
            && (this.getTotalRecharge() == null ? other.getTotalRecharge() == null : this.getTotalRecharge().equals(other.getTotalRecharge()))
            && (this.getTotalIncome() == null ? other.getTotalIncome() == null : this.getTotalIncome().equals(other.getTotalIncome()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getBalance() == null) ? 0 : getBalance().hashCode());
        result = prime * result + ((getFrozenBalance() == null) ? 0 : getFrozenBalance().hashCode());
        result = prime * result + ((getTotalRecharge() == null) ? 0 : getTotalRecharge().hashCode());
        result = prime * result + ((getTotalIncome() == null) ? 0 : getTotalIncome().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
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
        sb.append(", balance=").append(balance);
        sb.append(", frozenBalance=").append(frozenBalance);
        sb.append(", totalRecharge=").append(totalRecharge);
        sb.append(", totalIncome=").append(totalIncome);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
