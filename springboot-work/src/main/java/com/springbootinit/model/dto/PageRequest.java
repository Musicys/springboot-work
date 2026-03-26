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
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;

    /**
     * 构造函数
     */
    public PageRequest() {
        this.pageNum = 1;
        this.pageSize = 10;
    }

    /**
     * 构造函数
     * @param pageNum 页码
     * @param pageSize 每页大小
     */
    public PageRequest(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum != null && pageNum > 0 ? pageNum : 1;
        this.pageSize = pageSize != null && pageSize > 0 ? pageSize : 10;
    }

}
