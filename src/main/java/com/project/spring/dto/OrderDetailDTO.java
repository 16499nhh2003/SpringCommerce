package com.project.spring.dto;

import com.project.spring.model.Order;
import com.project.spring.model.Product;
import lombok.Data;

@Data
public class OrderDetailDTO {
    private Long idProduct;
    private Long orderId;
    private String nameProduct;
    private double priceProduct;
    private int quantity;
}
