package com.springbootinit.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 订单搜索包装类
 */
@ApiModel(description = "订单搜索包装类")
public class OrderSearchWrapper {

    @ApiModelProperty(value = "分页参数")
    private PageRequest page;

    @ApiModelProperty(value = "搜索参数")
    private OrderSearchParams params;

    public PageRequest getPage() {
        return page;
    }

    public void setPage(PageRequest page) {
        this.page = page;
    }

    public OrderSearchParams getParams() {
        return params;
    }

    public void setParams(OrderSearchParams params) {
        this.params = params;
    }
}
