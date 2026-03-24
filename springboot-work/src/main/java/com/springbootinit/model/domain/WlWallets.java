package com.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 钱包账户表
 * @TableName wl_wallets
 */
@TableName(value ="wl_wallets")
public class WlWallets implements Serializable {
    /**
     * 关联 ur_users.id
     */
    @TableId
    private Long userId;

    /**
     * 可用余额
     */
    private BigDecimal balance;

    /**
     * 冻结金额 (在押资金)
     */
    private BigDecimal frozenBalance;

    /**
     * 
     */
    private BigDecimal totalIncome;

    /**
     * 乐观锁，防止并发扣款
     */
    private Integer version;

    /**
     * 
     */
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 关联 ur_users.id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 关联 ur_users.id
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
     * 冻结金额 (在押资金)
     */
    public BigDecimal getFrozenBalance() {
        return frozenBalance;
    }

    /**
     * 冻结金额 (在押资金)
     */
    public void setFrozenBalance(BigDecimal frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    /**
     * 
     */
    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    /**
     * 
     */
    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    /**
     * 乐观锁，防止并发扣款
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * 乐观锁，防止并发扣款
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * 
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
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
        WlWallets other = (WlWallets) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getBalance() == null ? other.getBalance() == null : this.getBalance().equals(other.getBalance()))
            && (this.getFrozenBalance() == null ? other.getFrozenBalance() == null : this.getFrozenBalance().equals(other.getFrozenBalance()))
            && (this.getTotalIncome() == null ? other.getTotalIncome() == null : this.getTotalIncome().equals(other.getTotalIncome()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getBalance() == null) ? 0 : getBalance().hashCode());
        result = prime * result + ((getFrozenBalance() == null) ? 0 : getFrozenBalance().hashCode());
        result = prime * result + ((getTotalIncome() == null) ? 0 : getTotalIncome().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", balance=").append(balance);
        sb.append(", frozenBalance=").append(frozenBalance);
        sb.append(", totalIncome=").append(totalIncome);
        sb.append(", version=").append(version);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}