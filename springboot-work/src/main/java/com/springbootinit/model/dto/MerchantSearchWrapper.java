package com.springbootinit.model.dto;

import lombok.Data;

/**
 * 商户搜索请求包装类
 */
@Data
public class MerchantSearchWrapper {

    /**
     * 分页参数
     */
    private PageParams page;

    /**
     * 搜索参数
     */
    private MerchantSearchParams params;

    /**
     * 构造函数
     */
    public MerchantSearchWrapper() {
        this.page = new PageParams();
        this.params = new MerchantSearchParams();
    }

}
