package com.springbootinit.model.dto;

import lombok.Data;

@Data
public class TransactionLogsQueryDTO {

    private Integer pageNum;

    private Integer pageSize;

    private String keyword;

    private Long userId;

    private String type;

    private Long relatedOrderId;

    public TransactionLogsQueryDTO() {
        this.pageNum = 1;
        this.pageSize = 10;
    }

    public TransactionLogsQueryDTO(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum != null && pageNum > 0 ? pageNum : 1;
        this.pageSize = pageSize != null && pageSize > 0 ? pageSize : 10;
    }
}