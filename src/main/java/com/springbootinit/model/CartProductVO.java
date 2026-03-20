package com.springbootinit.model;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartProductVO {
    // 来自 cart 表
    private Long cartId;
    private Integer quantity;

    // 来自 product 表
    private Long productId;
    private String name;
    private BigDecimal price;
    private String description;
    private List<String> carouselImages;
}
