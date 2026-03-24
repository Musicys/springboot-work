package com.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 投诉主表
 * @TableName ds_complaints
 */
@TableName(value ="ds_complaints")
public class DsComplaints implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long complainantId;

    /**
     * 
     */
    private Long defendantId;

    /**
     * 1:商家，2:用户
     */
    private Integer targetType;

    /**
     * 
     */
    private Long relatedOrderId;

    /**
     * 
     */
    private String reasonCode;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private Object evidenceUrls;

    /**
     * 1:待处理，2:处理中，3:成功(处罚), 4:失败(证据不足)
     */
    private Integer status;

    /**
     * 处罚结果描述
     */
    private String resultAction;

    /**
     * 
     */
    private Long handledByAdminId;

    /**
     * 
     */
    private Date createdAt;

    /**
     * 
     */
    private Date closedAt;

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
    public Long getComplainantId() {
        return complainantId;
    }

    /**
     * 
     */
    public void setComplainantId(Long complainantId) {
        this.complainantId = complainantId;
    }

    /**
     * 
     */
    public Long getDefendantId() {
        return defendantId;
    }

    /**
     * 
     */
    public void setDefendantId(Long defendantId) {
        this.defendantId = defendantId;
    }

    /**
     * 1:商家，2:用户
     */
    public Integer getTargetType() {
        return targetType;
    }

    /**
     * 1:商家，2:用户
     */
    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
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
    public String getReasonCode() {
        return reasonCode;
    }

    /**
     * 
     */
    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
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
    public Object getEvidenceUrls() {
        return evidenceUrls;
    }

    /**
     * 
     */
    public void setEvidenceUrls(Object evidenceUrls) {
        this.evidenceUrls = evidenceUrls;
    }

    /**
     * 1:待处理，2:处理中，3:成功(处罚), 4:失败(证据不足)
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 1:待处理，2:处理中，3:成功(处罚), 4:失败(证据不足)
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 处罚结果描述
     */
    public String getResultAction() {
        return resultAction;
    }

    /**
     * 处罚结果描述
     */
    public void setResultAction(String resultAction) {
        this.resultAction = resultAction;
    }

    /**
     * 
     */
    public Long getHandledByAdminId() {
        return handledByAdminId;
    }

    /**
     * 
     */
    public void setHandledByAdminId(Long handledByAdminId) {
        this.handledByAdminId = handledByAdminId;
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
    public Date getClosedAt() {
        return closedAt;
    }

    /**
     * 
     */
    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
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
        DsComplaints other = (DsComplaints) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getComplainantId() == null ? other.getComplainantId() == null : this.getComplainantId().equals(other.getComplainantId()))
            && (this.getDefendantId() == null ? other.getDefendantId() == null : this.getDefendantId().equals(other.getDefendantId()))
            && (this.getTargetType() == null ? other.getTargetType() == null : this.getTargetType().equals(other.getTargetType()))
            && (this.getRelatedOrderId() == null ? other.getRelatedOrderId() == null : this.getRelatedOrderId().equals(other.getRelatedOrderId()))
            && (this.getReasonCode() == null ? other.getReasonCode() == null : this.getReasonCode().equals(other.getReasonCode()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getEvidenceUrls() == null ? other.getEvidenceUrls() == null : this.getEvidenceUrls().equals(other.getEvidenceUrls()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getResultAction() == null ? other.getResultAction() == null : this.getResultAction().equals(other.getResultAction()))
            && (this.getHandledByAdminId() == null ? other.getHandledByAdminId() == null : this.getHandledByAdminId().equals(other.getHandledByAdminId()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getClosedAt() == null ? other.getClosedAt() == null : this.getClosedAt().equals(other.getClosedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getComplainantId() == null) ? 0 : getComplainantId().hashCode());
        result = prime * result + ((getDefendantId() == null) ? 0 : getDefendantId().hashCode());
        result = prime * result + ((getTargetType() == null) ? 0 : getTargetType().hashCode());
        result = prime * result + ((getRelatedOrderId() == null) ? 0 : getRelatedOrderId().hashCode());
        result = prime * result + ((getReasonCode() == null) ? 0 : getReasonCode().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getEvidenceUrls() == null) ? 0 : getEvidenceUrls().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getResultAction() == null) ? 0 : getResultAction().hashCode());
        result = prime * result + ((getHandledByAdminId() == null) ? 0 : getHandledByAdminId().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getClosedAt() == null) ? 0 : getClosedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", complainantId=").append(complainantId);
        sb.append(", defendantId=").append(defendantId);
        sb.append(", targetType=").append(targetType);
        sb.append(", relatedOrderId=").append(relatedOrderId);
        sb.append(", reasonCode=").append(reasonCode);
        sb.append(", description=").append(description);
        sb.append(", evidenceUrls=").append(evidenceUrls);
        sb.append(", status=").append(status);
        sb.append(", resultAction=").append(resultAction);
        sb.append(", handledByAdminId=").append(handledByAdminId);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", closedAt=").append(closedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}