package com.springbootinit.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 兼职岗位搜索参数类
 */
@ApiModel(description = "兼职岗位搜索参数类")
public class JobSearchParams {

    @ApiModelProperty(value = "岗位标题")
    private String title;

    @ApiModelProperty(value = "商家ID")
    private Long merchantId;

    @ApiModelProperty(value = "分类ID")
    private Integer categoryId;

    @ApiModelProperty(value = "地区代码")
    private String regionCode;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "交易模式")
    private Integer tradeMode;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTradeMode() {
        return tradeMode;
    }

    public void setTradeMode(Integer tradeMode) {
        this.tradeMode = tradeMode;
    }

}