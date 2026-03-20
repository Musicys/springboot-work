package com.yupi.springbootinit.model.dto;


import lombok.Data;

@Data
public class ReduceCartDTO {
    private Long userId;
    private Long productId;
    private Integer quantity; // 可选，默认为1
}
