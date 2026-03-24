package com.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 信誉分主表
 * @TableName ct_credit_scores
 */
@TableName(value ="ct_credit_scores")
public class CtCreditScores implements Serializable {
    /**
     * 关联 ur_users.id
     */
    @TableId
    private Long userId;

    /**
     * 当前分数，默认 80
     */
    private Integer currentScore;

    /**
     * 最低阈值，低于此值限制发帖
     */
    private Integer minThreshold;

    /**
     * 
     */
    private Date lastUpdatedAt;

    /**
     * 乐观锁版本号
     */
    private Integer version;

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
     * 当前分数，默认 80
     */
    public Integer getCurrentScore() {
        return currentScore;
    }

    /**
     * 当前分数，默认 80
     */
    public void setCurrentScore(Integer currentScore) {
        this.currentScore = currentScore;
    }

    /**
     * 最低阈值，低于此值限制发帖
     */
    public Integer getMinThreshold() {
        return minThreshold;
    }

    /**
     * 最低阈值，低于此值限制发帖
     */
    public void setMinThreshold(Integer minThreshold) {
        this.minThreshold = minThreshold;
    }

    /**
     * 
     */
    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    /**
     * 
     */
    public void setLastUpdatedAt(Date lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    /**
     * 乐观锁版本号
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * 乐观锁版本号
     */
    public void setVersion(Integer version) {
        this.version = version;
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
        CtCreditScores other = (CtCreditScores) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getCurrentScore() == null ? other.getCurrentScore() == null : this.getCurrentScore().equals(other.getCurrentScore()))
            && (this.getMinThreshold() == null ? other.getMinThreshold() == null : this.getMinThreshold().equals(other.getMinThreshold()))
            && (this.getLastUpdatedAt() == null ? other.getLastUpdatedAt() == null : this.getLastUpdatedAt().equals(other.getLastUpdatedAt()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCurrentScore() == null) ? 0 : getCurrentScore().hashCode());
        result = prime * result + ((getMinThreshold() == null) ? 0 : getMinThreshold().hashCode());
        result = prime * result + ((getLastUpdatedAt() == null) ? 0 : getLastUpdatedAt().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", currentScore=").append(currentScore);
        sb.append(", minThreshold=").append(minThreshold);
        sb.append(", lastUpdatedAt=").append(lastUpdatedAt);
        sb.append(", version=").append(version);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}