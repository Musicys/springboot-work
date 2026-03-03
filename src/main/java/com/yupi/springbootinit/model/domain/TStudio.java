package com.yupi.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 工作室信息表
 * @TableName t_studio
 */
@TableName(value ="t_studio")
public class TStudio implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 工作室名称
     */
    private String name;

    /**
     * 负责人用户ID
     */
    private Long ownerId;

    /**
     * 工作室简介
     */
    private String description;

    /**
     * 营业执照/组织证明图片
     */
    private String licenseImg;

    /**
     * 状态: 0-审核中, 1-运营中, 2-已关闭
     */
    private Integer status;

    /**
     * 工作室整体信誉分
     */
    private Integer creditScore;

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
     * 工作室名称
     */
    public String getName() {
        return name;
    }

    /**
     * 工作室名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 负责人用户ID
     */
    public Long getOwnerId() {
        return ownerId;
    }

    /**
     * 负责人用户ID
     */
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * 工作室简介
     */
    public String getDescription() {
        return description;
    }

    /**
     * 工作室简介
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 营业执照/组织证明图片
     */
    public String getLicenseImg() {
        return licenseImg;
    }

    /**
     * 营业执照/组织证明图片
     */
    public void setLicenseImg(String licenseImg) {
        this.licenseImg = licenseImg;
    }

    /**
     * 状态: 0-审核中, 1-运营中, 2-已关闭
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 状态: 0-审核中, 1-运营中, 2-已关闭
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 工作室整体信誉分
     */
    public Integer getCreditScore() {
        return creditScore;
    }

    /**
     * 工作室整体信誉分
     */
    public void setCreditScore(Integer creditScore) {
        this.creditScore = creditScore;
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
        TStudio other = (TStudio) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getOwnerId() == null ? other.getOwnerId() == null : this.getOwnerId().equals(other.getOwnerId()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getLicenseImg() == null ? other.getLicenseImg() == null : this.getLicenseImg().equals(other.getLicenseImg()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreditScore() == null ? other.getCreditScore() == null : this.getCreditScore().equals(other.getCreditScore()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getOwnerId() == null) ? 0 : getOwnerId().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getLicenseImg() == null) ? 0 : getLicenseImg().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreditScore() == null) ? 0 : getCreditScore().hashCode());
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
        sb.append(", name=").append(name);
        sb.append(", ownerId=").append(ownerId);
        sb.append(", description=").append(description);
        sb.append(", licenseImg=").append(licenseImg);
        sb.append(", status=").append(status);
        sb.append(", creditScore=").append(creditScore);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
