package com.springbootinit.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商家发布兼职请求DTO
 */
@Data
public class ProJobPublishRequest {

    private Long Id;
    /**
     * 兼职标题
     */
    private String title;

    /**
     * 兼职描述
     */
    private String description;

    /**
     * 最低薪资
     */
    private BigDecimal salaryMin;

    /**
     * 最高薪资
     */
    private BigDecimal salaryMax;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 地区编码
     */
    private String regionCode;

    /**
     * 地区名称
     */
    private String regionName;

    /**
     * 押金金额
     */
    private BigDecimal depositAmount;

    /**
     * 结算周期
     */
    private String settlementCycle;

    /**
     * 工作时间类型
     */
    private Integer workTimeType;

    private String CoverImages;
    /**
     * 工作时间描述
     */
    private String workTimeDesc;

    /**
     * 兼职类型
     */
    private Integer jobType;

    /**
     * 招聘人数
     */
    private Integer recruitNum;

    /**
     * 简短介绍
     */
    private String briefIntro;

    /**
     * 交易模式
     */
    private Integer tradeMode;
}
