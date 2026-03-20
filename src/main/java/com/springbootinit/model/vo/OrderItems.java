package com.yupi.springbootinit.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItems {
    @JsonProperty("productId")
    private Long productId;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("price")
    private BigDecimal price;

    private String name; // 非数据库字段，仅用于展示
}
