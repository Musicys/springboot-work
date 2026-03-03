package com.yupi.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 兼职订单表
 * @TableName t_order
 */
@TableName(value ="t_order")
public class TOrder implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 兼职任务ID
     */
    private Long jobId;

    /**
     * 雇主ID
     */
    private Long publisherId;

    /**
     * 接单人ID
     */
    private Long acceptorId;

    /**
     * 接单工作室ID
     */
    private Long studioId;

    /**
     * 状态: 0-待确认, 1-进行中, 2-待验收, 3-已完成, 4-已取消, 5-纠纷中
     */
    private Integer status;

    /**
     * 接单时间
     */
    private Date agreeTime;

    /**
     * 完成提交时间
     */
    private Date finishTime;

    /**
     * 雇主确认时间
     */
    private Date confirmTime;

    /**
     * 约定薪资
     */
    private BigDecimal salaryAmount;

    /**
     * 押金状态: 0-未扣, 1-已冻结, 2-已退还, 3-已没收
     */
    private Integer depositStatus;

    /**
     * 评分 (1-5)
     */
    private Integer rating;

    /**
     * 评价内容
     */
    private String comment;

    /**
     *
     */
    private String cancelReason;

    /**
     *
     */
    private Integer deleted;

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
     * 订单号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 订单号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 兼职任务ID
     */
    public Long getJobId() {
        return jobId;
    }

    /**
     * 兼职任务ID
     */
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    /**
     * 雇主ID
     */
    public Long getPublisherId() {
        return publisherId;
    }

    /**
     * 雇主ID
     */
    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    /**
     * 接单人ID
     */
    public Long getAcceptorId() {
        return acceptorId;
    }

    /**
     * 接单人ID
     */
    public void setAcceptorId(Long acceptorId) {
        this.acceptorId = acceptorId;
    }

    /**
     * 接单工作室ID
     */
    public Long getStudioId() {
        return studioId;
    }

    /**
     * 接单工作室ID
     */
    public void setStudioId(Long studioId) {
        this.studioId = studioId;
    }

    /**
     * 状态: 0-待确认, 1-进行中, 2-待验收, 3-已完成, 4-已取消, 5-纠纷中
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 状态: 0-待确认, 1-进行中, 2-待验收, 3-已完成, 4-已取消, 5-纠纷中
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 接单时间
     */
    public Date getAgreeTime() {
        return agreeTime;
    }

    /**
     * 接单时间
     */
    public void setAgreeTime(Date agreeTime) {
        this.agreeTime = agreeTime;
    }

    /**
     * 完成提交时间
     */
    public Date getFinishTime() {
        return finishTime;
    }

    /**
     * 完成提交时间
     */
    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    /**
     * 雇主确认时间
     */
    public Date getConfirmTime() {
        return confirmTime;
    }

    /**
     * 雇主确认时间
     */
    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    /**
     * 约定薪资
     */
    public BigDecimal getSalaryAmount() {
        return salaryAmount;
    }

    /**
     * 约定薪资
     */
    public void setSalaryAmount(BigDecimal salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    /**
     * 押金状态: 0-未扣, 1-已冻结, 2-已退还, 3-已没收
     */
    public Integer getDepositStatus() {
        return depositStatus;
    }

    /**
     * 押金状态: 0-未扣, 1-已冻结, 2-已退还, 3-已没收
     */
    public void setDepositStatus(Integer depositStatus) {
        this.depositStatus = depositStatus;
    }

    /**
     * 评分 (1-5)
     */
    public Integer getRating() {
        return rating;
    }

    /**
     * 评分 (1-5)
     */
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    /**
     * 评价内容
     */
    public String getComment() {
        return comment;
    }

    /**
     * 评价内容
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     *
     */
    public String getCancelReason() {
        return cancelReason;
    }

    /**
     *
     */
    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    /**
     *
     */
    public Integer getDeleted() {
        return deleted;
    }

    /**
     *
     */
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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
        TOrder other = (TOrder) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOrderNo() == null ? other.getOrderNo() == null : this.getOrderNo().equals(other.getOrderNo()))
            && (this.getJobId() == null ? other.getJobId() == null : this.getJobId().equals(other.getJobId()))
            && (this.getPublisherId() == null ? other.getPublisherId() == null : this.getPublisherId().equals(other.getPublisherId()))
            && (this.getAcceptorId() == null ? other.getAcceptorId() == null : this.getAcceptorId().equals(other.getAcceptorId()))
            && (this.getStudioId() == null ? other.getStudioId() == null : this.getStudioId().equals(other.getStudioId()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getAgreeTime() == null ? other.getAgreeTime() == null : this.getAgreeTime().equals(other.getAgreeTime()))
            && (this.getFinishTime() == null ? other.getFinishTime() == null : this.getFinishTime().equals(other.getFinishTime()))
            && (this.getConfirmTime() == null ? other.getConfirmTime() == null : this.getConfirmTime().equals(other.getConfirmTime()))
            && (this.getSalaryAmount() == null ? other.getSalaryAmount() == null : this.getSalaryAmount().equals(other.getSalaryAmount()))
            && (this.getDepositStatus() == null ? other.getDepositStatus() == null : this.getDepositStatus().equals(other.getDepositStatus()))
            && (this.getRating() == null ? other.getRating() == null : this.getRating().equals(other.getRating()))
            && (this.getComment() == null ? other.getComment() == null : this.getComment().equals(other.getComment()))
            && (this.getCancelReason() == null ? other.getCancelReason() == null : this.getCancelReason().equals(other.getCancelReason()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOrderNo() == null) ? 0 : getOrderNo().hashCode());
        result = prime * result + ((getJobId() == null) ? 0 : getJobId().hashCode());
        result = prime * result + ((getPublisherId() == null) ? 0 : getPublisherId().hashCode());
        result = prime * result + ((getAcceptorId() == null) ? 0 : getAcceptorId().hashCode());
        result = prime * result + ((getStudioId() == null) ? 0 : getStudioId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getAgreeTime() == null) ? 0 : getAgreeTime().hashCode());
        result = prime * result + ((getFinishTime() == null) ? 0 : getFinishTime().hashCode());
        result = prime * result + ((getConfirmTime() == null) ? 0 : getConfirmTime().hashCode());
        result = prime * result + ((getSalaryAmount() == null) ? 0 : getSalaryAmount().hashCode());
        result = prime * result + ((getDepositStatus() == null) ? 0 : getDepositStatus().hashCode());
        result = prime * result + ((getRating() == null) ? 0 : getRating().hashCode());
        result = prime * result + ((getComment() == null) ? 0 : getComment().hashCode());
        result = prime * result + ((getCancelReason() == null) ? 0 : getCancelReason().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
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
        sb.append(", orderNo=").append(orderNo);
        sb.append(", jobId=").append(jobId);
        sb.append(", publisherId=").append(publisherId);
        sb.append(", acceptorId=").append(acceptorId);
        sb.append(", studioId=").append(studioId);
        sb.append(", status=").append(status);
        sb.append(", agreeTime=").append(agreeTime);
        sb.append(", finishTime=").append(finishTime);
        sb.append(", confirmTime=").append(confirmTime);
        sb.append(", salaryAmount=").append(salaryAmount);
        sb.append(", depositStatus=").append(depositStatus);
        sb.append(", rating=").append(rating);
        sb.append(", comment=").append(comment);
        sb.append(", cancelReason=").append(cancelReason);
        sb.append(", deleted=").append(deleted);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
