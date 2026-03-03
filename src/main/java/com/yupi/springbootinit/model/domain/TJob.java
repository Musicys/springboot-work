package com.yupi.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 兼职任务表
 * @TableName t_job
 */
@TableName(value ="t_job")
public class TJob implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发布者ID
     */
    private Long publisherId;

    /**
     * 发布类型: 1-个人, 2-工作室
     */
    private Integer publisherType;

    /**
     * 兼职标题
     */
    private String title;

    /**
     * 分类
     */
    private String category;

    /**
     * 详细描述
     */
    private String description;

    /**
     * 岗位要求
     */
    private String requirements;

    /**
     * 最低薪资
     */
    private BigDecimal salaryMin;

    /**
     * 最高薪资
     */
    private BigDecimal salaryMax;

    /**
     * 薪资单位
     */
    private String salaryUnit;

    /**
     * 工作地点
     */
    private String location;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 招聘人数
     */
    private Integer quota;

    /**
     * 已录用人数
     */
    private Integer hiredCount;

    /**
     * 发布需扣除的虚拟押金
     */
    private BigDecimal depositAmount;

    /**
     * 状态: 0-待审核, 1-上架中, 2-已满员, 3-已下架, 4-审核拒绝
     */
    private Integer status;

    /**
     *
     */
    private String rejectReason;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 逻辑删除
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
     * 发布者ID
     */
    public Long getPublisherId() {
        return publisherId;
    }

    /**
     * 发布者ID
     */
    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    /**
     * 发布类型: 1-个人, 2-工作室
     */
    public Integer getPublisherType() {
        return publisherType;
    }

    /**
     * 发布类型: 1-个人, 2-工作室
     */
    public void setPublisherType(Integer publisherType) {
        this.publisherType = publisherType;
    }

    /**
     * 兼职标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 兼职标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 分类
     */
    public String getCategory() {
        return category;
    }

    /**
     * 分类
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * 详细描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 详细描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 岗位要求
     */
    public String getRequirements() {
        return requirements;
    }

    /**
     * 岗位要求
     */
    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    /**
     * 最低薪资
     */
    public BigDecimal getSalaryMin() {
        return salaryMin;
    }

    /**
     * 最低薪资
     */
    public void setSalaryMin(BigDecimal salaryMin) {
        this.salaryMin = salaryMin;
    }

    /**
     * 最高薪资
     */
    public BigDecimal getSalaryMax() {
        return salaryMax;
    }

    /**
     * 最高薪资
     */
    public void setSalaryMax(BigDecimal salaryMax) {
        this.salaryMax = salaryMax;
    }

    /**
     * 薪资单位
     */
    public String getSalaryUnit() {
        return salaryUnit;
    }

    /**
     * 薪资单位
     */
    public void setSalaryUnit(String salaryUnit) {
        this.salaryUnit = salaryUnit;
    }

    /**
     * 工作地点
     */
    public String getLocation() {
        return location;
    }

    /**
     * 工作地点
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * 开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 招聘人数
     */
    public Integer getQuota() {
        return quota;
    }

    /**
     * 招聘人数
     */
    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    /**
     * 已录用人数
     */
    public Integer getHiredCount() {
        return hiredCount;
    }

    /**
     * 已录用人数
     */
    public void setHiredCount(Integer hiredCount) {
        this.hiredCount = hiredCount;
    }

    /**
     * 发布需扣除的虚拟押金
     */
    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    /**
     * 发布需扣除的虚拟押金
     */
    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    /**
     * 状态: 0-待审核, 1-上架中, 2-已满员, 3-已下架, 4-审核拒绝
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 状态: 0-待审核, 1-上架中, 2-已满员, 3-已下架, 4-审核拒绝
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     *
     */
    public String getRejectReason() {
        return rejectReason;
    }

    /**
     *
     */
    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    /**
     * 浏览量
     */
    public Integer getViewCount() {
        return viewCount;
    }

    /**
     * 浏览量
     */
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    /**
     * 逻辑删除
     */
    public Integer getDeleted() {
        return deleted;
    }

    /**
     * 逻辑删除
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
        TJob other = (TJob) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPublisherId() == null ? other.getPublisherId() == null : this.getPublisherId().equals(other.getPublisherId()))
            && (this.getPublisherType() == null ? other.getPublisherType() == null : this.getPublisherType().equals(other.getPublisherType()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getCategory() == null ? other.getCategory() == null : this.getCategory().equals(other.getCategory()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getRequirements() == null ? other.getRequirements() == null : this.getRequirements().equals(other.getRequirements()))
            && (this.getSalaryMin() == null ? other.getSalaryMin() == null : this.getSalaryMin().equals(other.getSalaryMin()))
            && (this.getSalaryMax() == null ? other.getSalaryMax() == null : this.getSalaryMax().equals(other.getSalaryMax()))
            && (this.getSalaryUnit() == null ? other.getSalaryUnit() == null : this.getSalaryUnit().equals(other.getSalaryUnit()))
            && (this.getLocation() == null ? other.getLocation() == null : this.getLocation().equals(other.getLocation()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getQuota() == null ? other.getQuota() == null : this.getQuota().equals(other.getQuota()))
            && (this.getHiredCount() == null ? other.getHiredCount() == null : this.getHiredCount().equals(other.getHiredCount()))
            && (this.getDepositAmount() == null ? other.getDepositAmount() == null : this.getDepositAmount().equals(other.getDepositAmount()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getRejectReason() == null ? other.getRejectReason() == null : this.getRejectReason().equals(other.getRejectReason()))
            && (this.getViewCount() == null ? other.getViewCount() == null : this.getViewCount().equals(other.getViewCount()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPublisherId() == null) ? 0 : getPublisherId().hashCode());
        result = prime * result + ((getPublisherType() == null) ? 0 : getPublisherType().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getCategory() == null) ? 0 : getCategory().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getRequirements() == null) ? 0 : getRequirements().hashCode());
        result = prime * result + ((getSalaryMin() == null) ? 0 : getSalaryMin().hashCode());
        result = prime * result + ((getSalaryMax() == null) ? 0 : getSalaryMax().hashCode());
        result = prime * result + ((getSalaryUnit() == null) ? 0 : getSalaryUnit().hashCode());
        result = prime * result + ((getLocation() == null) ? 0 : getLocation().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getQuota() == null) ? 0 : getQuota().hashCode());
        result = prime * result + ((getHiredCount() == null) ? 0 : getHiredCount().hashCode());
        result = prime * result + ((getDepositAmount() == null) ? 0 : getDepositAmount().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getRejectReason() == null) ? 0 : getRejectReason().hashCode());
        result = prime * result + ((getViewCount() == null) ? 0 : getViewCount().hashCode());
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
        sb.append(", publisherId=").append(publisherId);
        sb.append(", publisherType=").append(publisherType);
        sb.append(", title=").append(title);
        sb.append(", category=").append(category);
        sb.append(", description=").append(description);
        sb.append(", requirements=").append(requirements);
        sb.append(", salaryMin=").append(salaryMin);
        sb.append(", salaryMax=").append(salaryMax);
        sb.append(", salaryUnit=").append(salaryUnit);
        sb.append(", location=").append(location);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", quota=").append(quota);
        sb.append(", hiredCount=").append(hiredCount);
        sb.append(", depositAmount=").append(depositAmount);
        sb.append(", status=").append(status);
        sb.append(", rejectReason=").append(rejectReason);
        sb.append(", viewCount=").append(viewCount);
        sb.append(", deleted=").append(deleted);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
