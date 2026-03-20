package com.springbootinit.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class CheckoutDTO {
    private Long userId;
    private String address;
    private List<Long> productIds; // 要结算的商品ID列表
}
