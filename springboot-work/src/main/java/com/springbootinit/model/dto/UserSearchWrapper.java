package com.springbootinit.model.dto;

import lombok.Data;

/**
 * 用户搜索请求包装类
 */
@Data
public class UserSearchWrapper {

    /**
     * 分页参数
     */
    private PageParams page;

    /**
     * 搜索参数
     */
    private UserSearchParams params;

    /**
     * 构造函数
     */
    public UserSearchWrapper() {
        this.page = new PageParams();
        this.params = new UserSearchParams();
    }

}
