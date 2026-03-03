package com.yupi.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 举报投诉表
 * @TableName t_report
 */
@TableName(value ="t_report")
public class TReport implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 举报人ID
     */
    private Long reporterId;

    /**
     * 被举报对象ID
     */
    private Long targetId;

    /**
     * 类型: 1-用户, 2-订单, 3-兼职
     */
    private Integer targetType;

    /**
     * 举报原因
     */
    private String reason;

    /**
     * 证据图片JSON数组
     */
    private String evidenceImgs;

    /**
     * 处理状态: 0-待处理, 1-处理中, 2-已解决, 3-无效举报
     */
    private Integer status;

    /**
     * 处理管理员ID
     */
    private Long handlerId;

    /**
     * 处理结果
     */
    private String resultDesc;

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
     * 举报人ID
     */
    public Long getReporterId() {
        return reporterId;
    }

    /**
     * 举报人ID
     */
    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
    }

    /**
     * 被举报对象ID
     */
    public Long getTargetId() {
        return targetId;
    }

    /**
     * 被举报对象ID
     */
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    /**
     * 类型: 1-用户, 2-订单, 3-兼职
     */
    public Integer getTargetType() {
        return targetType;
    }

    /**
     * 类型: 1-用户, 2-订单, 3-兼职
     */
    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }

    /**
     * 举报原因
     */
    public String getReason() {
        return reason;
    }

    /**
     * 举报原因
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * 证据图片JSON数组
     */
    public String getEvidenceImgs() {
        return evidenceImgs;
    }

    /**
     * 证据图片JSON数组
     */
    public void setEvidenceImgs(String evidenceImgs) {
        this.evidenceImgs = evidenceImgs;
    }

    /**
     * 处理状态: 0-待处理, 1-处理中, 2-已解决, 3-无效举报
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 处理状态: 0-待处理, 1-处理中, 2-已解决, 3-无效举报
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 处理管理员ID
     */
    public Long getHandlerId() {
        return handlerId;
    }

    /**
     * 处理管理员ID
     */
    public void setHandlerId(Long handlerId) {
        this.handlerId = handlerId;
    }

    /**
     * 处理结果
     */
    public String getResultDesc() {
        return resultDesc;
    }

    /**
     * 处理结果
     */
    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
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
        TReport other = (TReport) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getReporterId() == null ? other.getReporterId() == null : this.getReporterId().equals(other.getReporterId()))
            && (this.getTargetId() == null ? other.getTargetId() == null : this.getTargetId().equals(other.getTargetId()))
            && (this.getTargetType() == null ? other.getTargetType() == null : this.getTargetType().equals(other.getTargetType()))
            && (this.getReason() == null ? other.getReason() == null : this.getReason().equals(other.getReason()))
            && (this.getEvidenceImgs() == null ? other.getEvidenceImgs() == null : this.getEvidenceImgs().equals(other.getEvidenceImgs()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getHandlerId() == null ? other.getHandlerId() == null : this.getHandlerId().equals(other.getHandlerId()))
            && (this.getResultDesc() == null ? other.getResultDesc() == null : this.getResultDesc().equals(other.getResultDesc()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getReporterId() == null) ? 0 : getReporterId().hashCode());
        result = prime * result + ((getTargetId() == null) ? 0 : getTargetId().hashCode());
        result = prime * result + ((getTargetType() == null) ? 0 : getTargetType().hashCode());
        result = prime * result + ((getReason() == null) ? 0 : getReason().hashCode());
        result = prime * result + ((getEvidenceImgs() == null) ? 0 : getEvidenceImgs().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getHandlerId() == null) ? 0 : getHandlerId().hashCode());
        result = prime * result + ((getResultDesc() == null) ? 0 : getResultDesc().hashCode());
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
        sb.append(", reporterId=").append(reporterId);
        sb.append(", targetId=").append(targetId);
        sb.append(", targetType=").append(targetType);
        sb.append(", reason=").append(reason);
        sb.append(", evidenceImgs=").append(evidenceImgs);
        sb.append(", status=").append(status);
        sb.append(", handlerId=").append(handlerId);
        sb.append(", resultDesc=").append(resultDesc);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
