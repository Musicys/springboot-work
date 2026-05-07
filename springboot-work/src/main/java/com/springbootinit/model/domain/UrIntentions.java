package com.springbootinit.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 求职意向表
 * @TableName ur_intentions
 */
@TableName(value = "ur_intentions")
@Data // 自动生成 getter、setter、toString、equals、hashCode 等方法
@NoArgsConstructor // 自动生成无参构造方法
@AllArgsConstructor // 自动生成全参构造方法
public class UrIntentions implements Serializable {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 学号
     */
    private String studentId;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别（如：0-未知，1-男，2-女）
     */
    private Integer gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 职业
     */
    private String profession;

    /**
     * 个人简介
     */
    private String introduction;

    /**
     * 求职意向标签，如：周末可做，接受夜班
     */
    private String tagName;


    /**
     * 信誉分
     */
    private Integer creditScore;
    /**
     * 创建时间
     */
    private Date createdAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
