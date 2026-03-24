package com.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 仲裁案件表
 * @TableName ds_arbitration_cases
 */
@TableName(value ="ds_arbitration_cases")
public class DsArbitrationCases implements Serializable {
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
     * 
     */
    private Long initiatorId;

    /**
     * 
     */
    private String userEvidenceSummary;

    /**
     * 
     */
    private String merchantEvidenceSummary;

    /**
     * 管理员判词
     */
    private String adminComment;

    /**
     * 1:全额结款，2:部分结款，3:不结款，4:退还押金
     */
    private Integer rulingResult;

    /**
     * 
     */
    private BigDecimal finalSettlementAmount;

    /**
     * 
     */
    private Long handledByAdminId;

    /**
     * 1:审理中，2:已结案
     */
    private Integer status;

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
     * 
     */
    public Long getInitiatorId() {
        return initiatorId;
    }

    /**
     * 
     */
    public void setInitiatorId(Long initiatorId) {
        this.initiatorId = initiatorId;
    }

    /**
     * 
     */
    public String getUserEvidenceSummary() {
        return userEvidenceSummary;
    }

    /**
     * 
     */
    public void setUserEvidenceSummary(String userEvidenceSummary) {
        this.userEvidenceSummary = userEvidenceSummary;
    }

    /**
     * 
     */
    public String getMerchantEvidenceSummary() {
        return merchantEvidenceSummary;
    }

    /**
     * 
     */
    public void setMerchantEvidenceSummary(String merchantEvidenceSummary) {
        this.merchantEvidenceSummary = merchantEvidenceSummary;
    }

    /**
     * 管理员判词
     */
    public String getAdminComment() {
        return adminComment;
    }

    /**
     * 管理员判词
     */
    public void setAdminComment(String adminComment) {
        this.adminComment = adminComment;
    }

    /**
     * 1:全额结款，2:部分结款，3:不结款，4:退还押金
     */
    public Integer getRulingResult() {
        return rulingResult;
    }

    /**
     * 1:全额结款，2:部分结款，3:不结款，4:退还押金
     */
    public void setRulingResult(Integer rulingResult) {
        this.rulingResult = rulingResult;
    }

    /**
     * 
     */
    public BigDecimal getFinalSettlementAmount() {
        return finalSettlementAmount;
    }

    /**
     * 
     */
    public void setFinalSettlementAmount(BigDecimal finalSettlementAmount) {
        this.finalSettlementAmount = finalSettlementAmount;
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
     * 1:审理中，2:已结案
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 1:审理中，2:已结案
     */
    public void setStatus(Integer status) {
        this.status = status;
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
        DsArbitrationCases other = (DsArbitrationCases) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getInitiatorId() == null ? other.getInitiatorId() == null : this.getInitiatorId().equals(other.getInitiatorId()))
            && (this.getUserEvidenceSummary() == null ? other.getUserEvidenceSummary() == null : this.getUserEvidenceSummary().equals(other.getUserEvidenceSummary()))
            && (this.getMerchantEvidenceSummary() == null ? other.getMerchantEvidenceSummary() == null : this.getMerchantEvidenceSummary().equals(other.getMerchantEvidenceSummary()))
            && (this.getAdminComment() == null ? other.getAdminComment() == null : this.getAdminComment().equals(other.getAdminComment()))
            && (this.getRulingResult() == null ? other.getRulingResult() == null : this.getRulingResult().equals(other.getRulingResult()))
            && (this.getFinalSettlementAmount() == null ? other.getFinalSettlementAmount() == null : this.getFinalSettlementAmount().equals(other.getFinalSettlementAmount()))
            && (this.getHandledByAdminId() == null ? other.getHandledByAdminId() == null : this.getHandledByAdminId().equals(other.getHandledByAdminId()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getClosedAt() == null ? other.getClosedAt() == null : this.getClosedAt().equals(other.getClosedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getInitiatorId() == null) ? 0 : getInitiatorId().hashCode());
        result = prime * result + ((getUserEvidenceSummary() == null) ? 0 : getUserEvidenceSummary().hashCode());
        result = prime * result + ((getMerchantEvidenceSummary() == null) ? 0 : getMerchantEvidenceSummary().hashCode());
        result = prime * result + ((getAdminComment() == null) ? 0 : getAdminComment().hashCode());
        result = prime * result + ((getRulingResult() == null) ? 0 : getRulingResult().hashCode());
        result = prime * result + ((getFinalSettlementAmount() == null) ? 0 : getFinalSettlementAmount().hashCode());
        result = prime * result + ((getHandledByAdminId() == null) ? 0 : getHandledByAdminId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
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
        sb.append(", orderId=").append(orderId);
        sb.append(", initiatorId=").append(initiatorId);
        sb.append(", userEvidenceSummary=").append(userEvidenceSummary);
        sb.append(", merchantEvidenceSummary=").append(merchantEvidenceSummary);
        sb.append(", adminComment=").append(adminComment);
        sb.append(", rulingResult=").append(rulingResult);
        sb.append(", finalSettlementAmount=").append(finalSettlementAmount);
        sb.append(", handledByAdminId=").append(handledByAdminId);
        sb.append(", status=").append(status);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", closedAt=").append(closedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}