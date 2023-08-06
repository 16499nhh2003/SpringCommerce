package com.project.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Long idCart;
    private Long idUser;
    private List<CartItemDTO> cartItems;
    private Double total;
}
