package com.yupi.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 工作室成员表
 * @TableName t_studio_member
 */
@TableName(value ="t_studio_member")
public class TStudioMember implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 工作室ID
     */
    private Long studioId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 成员角色: 1-负责人, 2-普通成员
     */
    private Integer role;

    /**
     * 加入状态: 0-申请中, 1-已通过, 2-已拒绝/退出
     */
    private Integer joinStatus;

    /**
     *
     */
    private Date joinTime;

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
     * 工作室ID
     */
    public Long getStudioId() {
        return studioId;
    }

    /**
     * 工作室ID
     */
    public void setStudioId(Long studioId) {
        this.studioId = studioId;
    }

    /**
     * 用户ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 成员角色: 1-负责人, 2-普通成员
     */
    public Integer getRole() {
        return role;
    }

    /**
     * 成员角色: 1-负责人, 2-普通成员
     */
    public void setRole(Integer role) {
        this.role = role;
    }

    /**
     * 加入状态: 0-申请中, 1-已通过, 2-已拒绝/退出
     */
    public Integer getJoinStatus() {
        return joinStatus;
    }

    /**
     * 加入状态: 0-申请中, 1-已通过, 2-已拒绝/退出
     */
    public void setJoinStatus(Integer joinStatus) {
        this.joinStatus = joinStatus;
    }

    /**
     *
     */
    public Date getJoinTime() {
        return joinTime;
    }

    /**
     *
     */
    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
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
        TStudioMember other = (TStudioMember) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getStudioId() == null ? other.getStudioId() == null : this.getStudioId().equals(other.getStudioId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getRole() == null ? other.getRole() == null : this.getRole().equals(other.getRole()))
            && (this.getJoinStatus() == null ? other.getJoinStatus() == null : this.getJoinStatus().equals(other.getJoinStatus()))
            && (this.getJoinTime() == null ? other.getJoinTime() == null : this.getJoinTime().equals(other.getJoinTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getStudioId() == null) ? 0 : getStudioId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getRole() == null) ? 0 : getRole().hashCode());
        result = prime * result + ((getJoinStatus() == null) ? 0 : getJoinStatus().hashCode());
        result = prime * result + ((getJoinTime() == null) ? 0 : getJoinTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", studioId=").append(studioId);
        sb.append(", userId=").append(userId);
        sb.append(", role=").append(role);
        sb.append(", joinStatus=").append(joinStatus);
        sb.append(", joinTime=").append(joinTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
