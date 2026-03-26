package com.springbootinit.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 管理员搜索包装类
 */
@ApiModel(description = "管理员搜索包装类")
public class AdminSearchWrapper {

    @ApiModelProperty(value = "分页参数")
    private PageRequest page;

    @ApiModelProperty(value = "搜索参数")
    private AdminSearchParams params;

    public PageRequest getPage() {
        return page;
    }

    public void setPage(PageRequest page) {
        this.page = page;
    }

    public AdminSearchParams getParams() {
        return params;
    }

    public void setParams(AdminSearchParams params) {
        this.params = params;
    }
}
