package com.project.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
	private Long productId;
	private String productName;
	private double productPrice;
    private int quantity;
	private String productOriginalPicture;

}
