package com.springbootinit.model.dto;

import lombok.Data;

/**
 * 商户搜索请求
 */
@Data
public class MerchantSearchRequest extends PageRequest {

    /**
     * 用户名
     */
    private String username;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 地区代码
     */
    private String regionCode;

    /**
     * 状态 (1:正常, 0:封禁, -1:注销)
     */
    private Integer status;

}
