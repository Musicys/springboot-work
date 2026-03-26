package com.springbootinit.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 兼职岗位搜索包装类
 */
@ApiModel(description = "兼职岗位搜索包装类")
public class JobSearchWrapper {

    @ApiModelProperty(value = "分页参数")
    private PageRequest page;

    @ApiModelProperty(value = "搜索参数")
    private JobSearchParams params;

    public PageRequest getPage() {
        return page;
    }

    public void setPage(PageRequest page) {
        this.page = page;
    }

    public JobSearchParams getParams() {
        return params;
    }

    public void setParams(JobSearchParams params) {
        this.params = params;
    }

}