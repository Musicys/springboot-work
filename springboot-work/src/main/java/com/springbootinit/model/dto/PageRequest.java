package com.springbootinit.model.dto;

import lombok.Data;

/**
 * 分页请求参数
 */
@Data
public class PageRequest {

    /**
     * 页码
     */
    private Integer page;

    /**
     * 每页大小
     */
    private Integer size;

    /**
     * 构造函数
     */
    public PageRequest() {
        this.page = 1;
        this.size = 10;
    }

    /**
     * 构造函数
     * @param page 页码
     * @param size 每页大小
     */
    public PageRequest(Integer page, Integer size) {
        this.page = page != null && page > 0 ? page : 1;
        this.size = size != null && size > 0 ? size : 10;
    }

}
