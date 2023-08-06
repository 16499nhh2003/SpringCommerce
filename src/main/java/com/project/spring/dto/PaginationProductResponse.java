package com.project.spring.dto;


import com.project.spring.model.Product;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginationProductResponse {
    private List<Product> products;
    private Long numberOfItems;
    private int numberOfPages;
    private int numberTotalPages;
}
