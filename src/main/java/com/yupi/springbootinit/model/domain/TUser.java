package com.yupi.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户基础表
 * @TableName t_user
 */
@TableName(value ="t_user")
public class TUser implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**@TableId
     *
     * 登录账号
     */
    private String username;

    /**
     * 加密密码 (BCrypt)
     */
    private String password;

    /**
     * 昵称/真实姓名
     */
    private String nickname;

    /**
     * 角色ID: 1-超管, 2-管理员, 3-普通用户, 4-工作室
     */
    private Long roleId;

    /**
     * 状态: 0-封禁, 1-正常
     */
    private Integer status;

    /**
     * 信誉分 (初始100，低于60限制接单)
     */
    private Integer creditScore;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 所属学校/学院ID
     */
    private Long schoolId;

    /**
     * 逻辑删除: 0-未删, 1-已删
     */
    private Integer deleted;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 主键ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 登录账号
     */
    public String getUsername() {
        return username;
    }

    /**
     * 登录账号
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 加密密码 (BCrypt)
     */
    public String getPassword() {
        return password;
    }

    /**
     * 加密密码 (BCrypt)
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 昵称/真实姓名
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 昵称/真实姓名
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 角色ID: 1-超管, 2-管理员, 3-普通用户, 4-工作室
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * 角色ID: 1-超管, 2-管理员, 3-普通用户, 4-工作室
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * 状态: 0-封禁, 1-正常
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 状态: 0-封禁, 1-正常
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 信誉分 (初始100，低于60限制接单)
     */
    public Integer getCreditScore() {
        return creditScore;
    }

    /**
     * 信誉分 (初始100，低于60限制接单)
     */
    public void setCreditScore(Integer creditScore) {
        this.creditScore = creditScore;
    }

    /**
     * 头像URL
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 头像URL
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 所属学校/学院ID
     */
    public Long getSchoolId() {
        return schoolId;
    }

    /**
     * 所属学校/学院ID
     */
    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    /**
     * 逻辑删除: 0-未删, 1-已删
     */
    public Integer getDeleted() {
        return deleted;
    }

    /**
     * 逻辑删除: 0-未删, 1-已删
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

    /**
     *
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     *
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
        TUser other = (TUser) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getNickname() == null ? other.getNickname() == null : this.getNickname().equals(other.getNickname()))
            && (this.getRoleId() == null ? other.getRoleId() == null : this.getRoleId().equals(other.getRoleId()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreditScore() == null ? other.getCreditScore() == null : this.getCreditScore().equals(other.getCreditScore()))
            && (this.getAvatar() == null ? other.getAvatar() == null : this.getAvatar().equals(other.getAvatar()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getSchoolId() == null ? other.getSchoolId() == null : this.getSchoolId().equals(other.getSchoolId()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getNickname() == null) ? 0 : getNickname().hashCode());
        result = prime * result + ((getRoleId() == null) ? 0 : getRoleId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreditScore() == null) ? 0 : getCreditScore().hashCode());
        result = prime * result + ((getAvatar() == null) ? 0 : getAvatar().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getSchoolId() == null) ? 0 : getSchoolId().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", nickname=").append(nickname);
        sb.append(", roleId=").append(roleId);
        sb.append(", status=").append(status);
        sb.append(", creditScore=").append(creditScore);
        sb.append(", avatar=").append(avatar);
        sb.append(", phone=").append(phone);
        sb.append(", schoolId=").append(schoolId);
        sb.append(", deleted=").append(deleted);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
