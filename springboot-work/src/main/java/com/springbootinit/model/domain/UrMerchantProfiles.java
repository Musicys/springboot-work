package com.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 商家档案表
 * @TableName ur_merchant_profiles
 */
@TableName(value ="ur_merchant_profiles")
public class UrMerchantProfiles implements Serializable {
    /**
     * 关联 ur_users.id
     */
    @TableId
    private Long userId;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 营业执照 URL
     */
    private String licenseUrl;

    /**
     * 父账号 ID (实现连坐机制，子账号指向主账号)
     */
    private Long parentMerchantId;

    /**
     * 0:主账号, 1:子账号
     */
    private Integer isSubAccount;

    /**
     * 
     */
    private String contactPhone;

    /**
     * 
     */
    private Date createdAt;

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
     * 企业名称
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 企业名称
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * 营业执照 URL
     */
    public String getLicenseUrl() {
        return licenseUrl;
    }

    /**
     * 营业执照 URL
     */
    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    /**
     * 父账号 ID (实现连坐机制，子账号指向主账号)
     */
    public Long getParentMerchantId() {
        return parentMerchantId;
    }

    /**
     * 父账号 ID (实现连坐机制，子账号指向主账号)
     */
    public void setParentMerchantId(Long parentMerchantId) {
        this.parentMerchantId = parentMerchantId;
    }

    /**
     * 0:主账号, 1:子账号
     */
    public Integer getIsSubAccount() {
        return isSubAccount;
    }

    /**
     * 0:主账号, 1:子账号
     */
    public void setIsSubAccount(Integer isSubAccount) {
        this.isSubAccount = isSubAccount;
    }

    /**
     * 
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * 
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
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
        UrMerchantProfiles other = (UrMerchantProfiles) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getCompanyName() == null ? other.getCompanyName() == null : this.getCompanyName().equals(other.getCompanyName()))
            && (this.getLicenseUrl() == null ? other.getLicenseUrl() == null : this.getLicenseUrl().equals(other.getLicenseUrl()))
            && (this.getParentMerchantId() == null ? other.getParentMerchantId() == null : this.getParentMerchantId().equals(other.getParentMerchantId()))
            && (this.getIsSubAccount() == null ? other.getIsSubAccount() == null : this.getIsSubAccount().equals(other.getIsSubAccount()))
            && (this.getContactPhone() == null ? other.getContactPhone() == null : this.getContactPhone().equals(other.getContactPhone()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCompanyName() == null) ? 0 : getCompanyName().hashCode());
        result = prime * result + ((getLicenseUrl() == null) ? 0 : getLicenseUrl().hashCode());
        result = prime * result + ((getParentMerchantId() == null) ? 0 : getParentMerchantId().hashCode());
        result = prime * result + ((getIsSubAccount() == null) ? 0 : getIsSubAccount().hashCode());
        result = prime * result + ((getContactPhone() == null) ? 0 : getContactPhone().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", companyName=").append(companyName);
        sb.append(", licenseUrl=").append(licenseUrl);
        sb.append(", parentMerchantId=").append(parentMerchantId);
        sb.append(", isSubAccount=").append(isSubAccount);
        sb.append(", contactPhone=").append(contactPhone);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}