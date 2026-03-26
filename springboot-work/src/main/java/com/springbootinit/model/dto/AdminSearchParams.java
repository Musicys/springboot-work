package com.springbootinit.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 管理员搜索参数类
 */
@ApiModel(description = "管理员搜索参数类")
public class AdminSearchParams {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "地区代码")
    private String regionCode;

    @ApiModelProperty(value = "状态")
    private Integer status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
