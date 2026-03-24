package com.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 订单证据表
 * @TableName od_evidence
 */
@TableName(value ="od_evidence")
public class OdEvidence implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long orderId;

    /**
     * 1:用户，2:商家
     */
    private Integer uploaderType;

    /**
     * 
     */
    private Long uploaderId;

    /**
     * 1:图片，2:视频，3:文本
     */
    private Integer evidenceType;

    /**
     * 
     */
    private String fileUrl;

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
     * 1:用户，2:商家
     */
    public Integer getUploaderType() {
        return uploaderType;
    }

    /**
     * 1:用户，2:商家
     */
    public void setUploaderType(Integer uploaderType) {
        this.uploaderType = uploaderType;
    }

    /**
     * 
     */
    public Long getUploaderId() {
        return uploaderId;
    }

    /**
     * 
     */
    public void setUploaderId(Long uploaderId) {
        this.uploaderId = uploaderId;
    }

    /**
     * 1:图片，2:视频，3:文本
     */
    public Integer getEvidenceType() {
        return evidenceType;
    }

    /**
     * 1:图片，2:视频，3:文本
     */
    public void setEvidenceType(Integer evidenceType) {
        this.evidenceType = evidenceType;
    }

    /**
     * 
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * 
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
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
        OdEvidence other = (OdEvidence) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getUploaderType() == null ? other.getUploaderType() == null : this.getUploaderType().equals(other.getUploaderType()))
            && (this.getUploaderId() == null ? other.getUploaderId() == null : this.getUploaderId().equals(other.getUploaderId()))
            && (this.getEvidenceType() == null ? other.getEvidenceType() == null : this.getEvidenceType().equals(other.getEvidenceType()))
            && (this.getFileUrl() == null ? other.getFileUrl() == null : this.getFileUrl().equals(other.getFileUrl()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getUploaderType() == null) ? 0 : getUploaderType().hashCode());
        result = prime * result + ((getUploaderId() == null) ? 0 : getUploaderId().hashCode());
        result = prime * result + ((getEvidenceType() == null) ? 0 : getEvidenceType().hashCode());
        result = prime * result + ((getFileUrl() == null) ? 0 : getFileUrl().hashCode());
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
        sb.append(", orderId=").append(orderId);
        sb.append(", uploaderType=").append(uploaderType);
        sb.append(", uploaderId=").append(uploaderId);
        sb.append(", evidenceType=").append(evidenceType);
        sb.append(", fileUrl=").append(fileUrl);
        sb.append(", description=").append(description);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}