package com.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 兼职岗位表
 * @TableName jb_jobs
 */
@TableName(value ="jb_jobs")
public class JbJobs implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发布者 ID
     */
    private Long merchantId;

    /**
     * 
     */
    private String title;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private BigDecimal salaryMin;

    /**
     * 
     */
    private BigDecimal salaryMax;

    /**
     * 
     */
    private Integer categoryId;

    /**
     * 
     */
    private String regionCode;

    /**
     * 押金金额 (商家担保)
     */
    private BigDecimal depositAmount;

    /**
     * 1:发布中，2:进行中，3:已完成，4:已关闭，5:强制下架
     */
    private Integer status;

    /**
     * 1:平台担保，2:允许线下
     */
    private Integer tradeMode;

    /**
     * 
     */
    private Date publishTime;

    /**
     * 
     */
    private Date expireTime;

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
     * 发布者 ID
     */
    public Long getMerchantId() {
        return merchantId;
    }

    /**
     * 发布者 ID
     */
    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    /**
     * 
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     */
    public void setTitle(String title) {
        this.title = title;
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
    public BigDecimal getSalaryMin() {
        return salaryMin;
    }

    /**
     * 
     */
    public void setSalaryMin(BigDecimal salaryMin) {
        this.salaryMin = salaryMin;
    }

    /**
     * 
     */
    public BigDecimal getSalaryMax() {
        return salaryMax;
    }

    /**
     * 
     */
    public void setSalaryMax(BigDecimal salaryMax) {
        this.salaryMax = salaryMax;
    }

    /**
     * 
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * 
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 
     */
    public String getRegionCode() {
        return regionCode;
    }

    /**
     * 
     */
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    /**
     * 押金金额 (商家担保)
     */
    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    /**
     * 押金金额 (商家担保)
     */
    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    /**
     * 1:发布中，2:进行中，3:已完成，4:已关闭，5:强制下架
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 1:发布中，2:进行中，3:已完成，4:已关闭，5:强制下架
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 1:平台担保，2:允许线下
     */
    public Integer getTradeMode() {
        return tradeMode;
    }

    /**
     * 1:平台担保，2:允许线下
     */
    public void setTradeMode(Integer tradeMode) {
        this.tradeMode = tradeMode;
    }

    /**
     * 
     */
    public Date getPublishTime() {
        return publishTime;
    }

    /**
     * 
     */
    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    /**
     * 
     */
    public Date getExpireTime() {
        return expireTime;
    }

    /**
     * 
     */
    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
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
        JbJobs other = (JbJobs) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMerchantId() == null ? other.getMerchantId() == null : this.getMerchantId().equals(other.getMerchantId()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getSalaryMin() == null ? other.getSalaryMin() == null : this.getSalaryMin().equals(other.getSalaryMin()))
            && (this.getSalaryMax() == null ? other.getSalaryMax() == null : this.getSalaryMax().equals(other.getSalaryMax()))
            && (this.getCategoryId() == null ? other.getCategoryId() == null : this.getCategoryId().equals(other.getCategoryId()))
            && (this.getRegionCode() == null ? other.getRegionCode() == null : this.getRegionCode().equals(other.getRegionCode()))
            && (this.getDepositAmount() == null ? other.getDepositAmount() == null : this.getDepositAmount().equals(other.getDepositAmount()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getTradeMode() == null ? other.getTradeMode() == null : this.getTradeMode().equals(other.getTradeMode()))
            && (this.getPublishTime() == null ? other.getPublishTime() == null : this.getPublishTime().equals(other.getPublishTime()))
            && (this.getExpireTime() == null ? other.getExpireTime() == null : this.getExpireTime().equals(other.getExpireTime()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMerchantId() == null) ? 0 : getMerchantId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getSalaryMin() == null) ? 0 : getSalaryMin().hashCode());
        result = prime * result + ((getSalaryMax() == null) ? 0 : getSalaryMax().hashCode());
        result = prime * result + ((getCategoryId() == null) ? 0 : getCategoryId().hashCode());
        result = prime * result + ((getRegionCode() == null) ? 0 : getRegionCode().hashCode());
        result = prime * result + ((getDepositAmount() == null) ? 0 : getDepositAmount().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getTradeMode() == null) ? 0 : getTradeMode().hashCode());
        result = prime * result + ((getPublishTime() == null) ? 0 : getPublishTime().hashCode());
        result = prime * result + ((getExpireTime() == null) ? 0 : getExpireTime().hashCode());
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
        sb.append(", merchantId=").append(merchantId);
        sb.append(", title=").append(title);
        sb.append(", description=").append(description);
        sb.append(", salaryMin=").append(salaryMin);
        sb.append(", salaryMax=").append(salaryMax);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", regionCode=").append(regionCode);
        sb.append(", depositAmount=").append(depositAmount);
        sb.append(", status=").append(status);
        sb.append(", tradeMode=").append(tradeMode);
        sb.append(", publishTime=").append(publishTime);
        sb.append(", expireTime=").append(expireTime);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}