package com.yupi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.yupi.springbootinit.model.vo.OrderItems;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @TableName app_order
 */
@Data
@TableName(value = "app_order", autoResultMap = true) // 关键：启用 resultMap 自动映射 JSON
public class AppOrder implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private Long userId;

    /**
     *
     */
    private String address;

    /**
     *
     */
    private BigDecimal totalAmount;

    /**
     * 商品详情列表 [{productId, quantity, price}, ...]
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<OrderItems> productDetails;

    /**
     *
     */
    private Date createdAt;



}
