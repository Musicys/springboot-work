package com.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 投递记录表
 * @TableName od_applications
 */
@TableName(value ="od_applications")
public class OdApplications implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long jobId;

    /**
     * 
     */
    private Long userId;

    /**
     * 1:已投递，2:面试中，3:已录用，4:已拒绝
     */
    private Integer status;

    /**
     * 投递时的简历快照
     */
    private Object resumeSnapshot;

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
    public Long getJobId() {
        return jobId;
    }

    /**
     * 
     */
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    /**
     * 
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 1:已投递，2:面试中，3:已录用，4:已拒绝
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 1:已投递，2:面试中，3:已录用，4:已拒绝
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 投递时的简历快照
     */
    public Object getResumeSnapshot() {
        return resumeSnapshot;
    }

    /**
     * 投递时的简历快照
     */
    public void setResumeSnapshot(Object resumeSnapshot) {
        this.resumeSnapshot = resumeSnapshot;
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
        OdApplications other = (OdApplications) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getJobId() == null ? other.getJobId() == null : this.getJobId().equals(other.getJobId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getResumeSnapshot() == null ? other.getResumeSnapshot() == null : this.getResumeSnapshot().equals(other.getResumeSnapshot()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getJobId() == null) ? 0 : getJobId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getResumeSnapshot() == null) ? 0 : getResumeSnapshot().hashCode());
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
        sb.append(", jobId=").append(jobId);
        sb.append(", userId=").append(userId);
        sb.append(", status=").append(status);
        sb.append(", resumeSnapshot=").append(resumeSnapshot);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}