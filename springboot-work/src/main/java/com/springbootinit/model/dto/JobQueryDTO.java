package com.springbootinit.model.dto;

import lombok.Data;

/**
 * 兼职查询DTO
 */
@Data
public class JobQueryDTO {

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;

    /**
     * 关键词搜索
     */
    private String keyword;

    /**
     * 构造函数
     */
    public JobQueryDTO() {
        this.pageNum = 1;
        this.pageSize = 10;
    }

    /**
     * 构造函数
     * @param pageNum 页码
     * @param pageSize 每页大小
     */
    public JobQueryDTO(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum != null && pageNum > 0 ? pageNum : 1;
        this.pageSize = pageSize != null && pageSize > 0 ? pageSize : 10;
    }
}