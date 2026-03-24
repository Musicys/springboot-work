package com.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 会话表
 * @TableName ms_conversations
 */
@TableName(value ="ms_conversations")
public class MsConversations implements Serializable {
    /**
     * 会话唯一 ID
     */
    @TableId
    private String sessionId;

    /**
     * [user_id, merchant_id]
     */
    private Object participantIds;

    /**
     * 关联的兼职 ID
     */
    private Long jobId;

    /**
     * 
     */
    private String lastMsgContent;

    /**
     * 
     */
    private Date lastMsgTime;

    /**
     * 
     */
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 会话唯一 ID
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * 会话唯一 ID
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * [user_id, merchant_id]
     */
    public Object getParticipantIds() {
        return participantIds;
    }

    /**
     * [user_id, merchant_id]
     */
    public void setParticipantIds(Object participantIds) {
        this.participantIds = participantIds;
    }

    /**
     * 关联的兼职 ID
     */
    public Long getJobId() {
        return jobId;
    }

    /**
     * 关联的兼职 ID
     */
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    /**
     * 
     */
    public String getLastMsgContent() {
        return lastMsgContent;
    }

    /**
     * 
     */
    public void setLastMsgContent(String lastMsgContent) {
        this.lastMsgContent = lastMsgContent;
    }

    /**
     * 
     */
    public Date getLastMsgTime() {
        return lastMsgTime;
    }

    /**
     * 
     */
    public void setLastMsgTime(Date lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }

    /**
     * 
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
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
        MsConversations other = (MsConversations) that;
        return (this.getSessionId() == null ? other.getSessionId() == null : this.getSessionId().equals(other.getSessionId()))
            && (this.getParticipantIds() == null ? other.getParticipantIds() == null : this.getParticipantIds().equals(other.getParticipantIds()))
            && (this.getJobId() == null ? other.getJobId() == null : this.getJobId().equals(other.getJobId()))
            && (this.getLastMsgContent() == null ? other.getLastMsgContent() == null : this.getLastMsgContent().equals(other.getLastMsgContent()))
            && (this.getLastMsgTime() == null ? other.getLastMsgTime() == null : this.getLastMsgTime().equals(other.getLastMsgTime()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSessionId() == null) ? 0 : getSessionId().hashCode());
        result = prime * result + ((getParticipantIds() == null) ? 0 : getParticipantIds().hashCode());
        result = prime * result + ((getJobId() == null) ? 0 : getJobId().hashCode());
        result = prime * result + ((getLastMsgContent() == null) ? 0 : getLastMsgContent().hashCode());
        result = prime * result + ((getLastMsgTime() == null) ? 0 : getLastMsgTime().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", sessionId=").append(sessionId);
        sb.append(", participantIds=").append(participantIds);
        sb.append(", jobId=").append(jobId);
        sb.append(", lastMsgContent=").append(lastMsgContent);
        sb.append(", lastMsgTime=").append(lastMsgTime);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}