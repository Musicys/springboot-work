package com.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户基础表
 * @TableName ur_users
 */
@TableName(value ="ur_users")
public class UrUsers implements Serializable {
    /**
     * 用户全局唯一 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 1:求职者(C), 2:商家(B), 3:管理员(Admin)
     */
    private Integer userType;

    /**
     * 用户名/手机号
     */
    private String username;

    /**
     * 加密密码
     */
    private String passwordHash;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 所在地区代码
     */
    private String regionCode;

    /**
     * 1:正常, 0:封禁, -1:注销
     */
    private Integer status;

    /**
     * 
     */
    private Date createdAt;

    /**
     * 
     */
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 用户全局唯一 ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 用户全局唯一 ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 1:求职者(C), 2:商家(B), 3:管理员(Admin)
     */
    public Integer getUserType() {
        return userType;
    }

    /**
     * 1:求职者(C), 2:商家(B), 3:管理员(Admin)
     */
    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    /**
     * 用户名/手机号
     */
    public String getUsername() {
        return username;
    }

    /**
     * 用户名/手机号
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 加密密码
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * 加密密码
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * 头像
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * 头像
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    /**
     * 所在地区代码
     */
    public String getRegionCode() {
        return regionCode;
    }

    /**
     * 所在地区代码
     */
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    /**
     * 1:正常, 0:封禁, -1:注销
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 1:正常, 0:封禁, -1:注销
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
        UrUsers other = (UrUsers) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserType() == null ? other.getUserType() == null : this.getUserType().equals(other.getUserType()))
            && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
            && (this.getPasswordHash() == null ? other.getPasswordHash() == null : this.getPasswordHash().equals(other.getPasswordHash()))
            && (this.getAvatarUrl() == null ? other.getAvatarUrl() == null : this.getAvatarUrl().equals(other.getAvatarUrl()))
            && (this.getRegionCode() == null ? other.getRegionCode() == null : this.getRegionCode().equals(other.getRegionCode()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserType() == null) ? 0 : getUserType().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getPasswordHash() == null) ? 0 : getPasswordHash().hashCode());
        result = prime * result + ((getAvatarUrl() == null) ? 0 : getAvatarUrl().hashCode());
        result = prime * result + ((getRegionCode() == null) ? 0 : getRegionCode().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userType=").append(userType);
        sb.append(", username=").append(username);
        sb.append(", passwordHash=").append(passwordHash);
        sb.append(", avatarUrl=").append(avatarUrl);
        sb.append(", regionCode=").append(regionCode);
        sb.append(", status=").append(status);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}