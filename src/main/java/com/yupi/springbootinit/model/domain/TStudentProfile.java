package com.yupi.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * 学生实名信息表
 * @TableName t_student_profile
 */
@TableName(value ="t_student_profile")
public class TStudentProfile implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联用户ID
     */
    private Long userId;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 学院
     */
    private String college;

    /**
     * 专业
     */
    private String major;

    /**
     * 年级
     */
    private String grade;

    /**
     * 身份证正面照URL
     */
    private String idCardFront;

    /**
     * 身份证反面照URL
     */
    private String idCardBack;

    /**
     * 认证状态: 0-待审核, 1-通过, 2-拒绝
     */
    private Integer authStatus;

    /**
     * 拒绝原因
     */
    private String rejectReason;

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
     * 关联用户ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 关联用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 学号
     */
    public String getStudentNo() {
        return studentNo;
    }

    /**
     * 学号
     */
    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    /**
     * 学院
     */
    public String getCollege() {
        return college;
    }

    /**
     * 学院
     */
    public void setCollege(String college) {
        this.college = college;
    }

    /**
     * 专业
     */
    public String getMajor() {
        return major;
    }

    /**
     * 专业
     */
    public void setMajor(String major) {
        this.major = major;
    }

    /**
     * 年级
     */
    public String getGrade() {
        return grade;
    }

    /**
     * 年级
     */
    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * 身份证正面照URL
     */
    public String getIdCardFront() {
        return idCardFront;
    }

    /**
     * 身份证正面照URL
     */
    public void setIdCardFront(String idCardFront) {
        this.idCardFront = idCardFront;
    }

    /**
     * 身份证反面照URL
     */
    public String getIdCardBack() {
        return idCardBack;
    }

    /**
     * 身份证反面照URL
     */
    public void setIdCardBack(String idCardBack) {
        this.idCardBack = idCardBack;
    }

    /**
     * 认证状态: 0-待审核, 1-通过, 2-拒绝
     */
    public Integer getAuthStatus() {
        return authStatus;
    }

    /**
     * 认证状态: 0-待审核, 1-通过, 2-拒绝
     */
    public void setAuthStatus(Integer authStatus) {
        this.authStatus = authStatus;
    }

    /**
     * 拒绝原因
     */
    public String getRejectReason() {
        return rejectReason;
    }

    /**
     * 拒绝原因
     */
    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
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
        TStudentProfile other = (TStudentProfile) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getStudentNo() == null ? other.getStudentNo() == null : this.getStudentNo().equals(other.getStudentNo()))
            && (this.getCollege() == null ? other.getCollege() == null : this.getCollege().equals(other.getCollege()))
            && (this.getMajor() == null ? other.getMajor() == null : this.getMajor().equals(other.getMajor()))
            && (this.getGrade() == null ? other.getGrade() == null : this.getGrade().equals(other.getGrade()))
            && (this.getIdCardFront() == null ? other.getIdCardFront() == null : this.getIdCardFront().equals(other.getIdCardFront()))
            && (this.getIdCardBack() == null ? other.getIdCardBack() == null : this.getIdCardBack().equals(other.getIdCardBack()))
            && (this.getAuthStatus() == null ? other.getAuthStatus() == null : this.getAuthStatus().equals(other.getAuthStatus()))
            && (this.getRejectReason() == null ? other.getRejectReason() == null : this.getRejectReason().equals(other.getRejectReason()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getStudentNo() == null) ? 0 : getStudentNo().hashCode());
        result = prime * result + ((getCollege() == null) ? 0 : getCollege().hashCode());
        result = prime * result + ((getMajor() == null) ? 0 : getMajor().hashCode());
        result = prime * result + ((getGrade() == null) ? 0 : getGrade().hashCode());
        result = prime * result + ((getIdCardFront() == null) ? 0 : getIdCardFront().hashCode());
        result = prime * result + ((getIdCardBack() == null) ? 0 : getIdCardBack().hashCode());
        result = prime * result + ((getAuthStatus() == null) ? 0 : getAuthStatus().hashCode());
        result = prime * result + ((getRejectReason() == null) ? 0 : getRejectReason().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", studentNo=").append(studentNo);
        sb.append(", college=").append(college);
        sb.append(", major=").append(major);
        sb.append(", grade=").append(grade);
        sb.append(", idCardFront=").append(idCardFront);
        sb.append(", idCardBack=").append(idCardBack);
        sb.append(", authStatus=").append(authStatus);
        sb.append(", rejectReason=").append(rejectReason);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
