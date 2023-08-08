package com.project.spring.service;

import com.project.spring.dto.PaginationProductResponse;
import com.project.spring.model.Category;
import com.project.spring.model.Manufacture;
import com.project.spring.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public interface ProductService {
    PaginationProductResponse getAllProduct(Pageable pageable);

    List<Product> getAllProducts();

    Optional<Product> getProductById(Long id);

    List<Product> getAllProductByCategory(Long id);

    List<Product> getAllProductByManufacture(Long id);

    Product addOrUpdate(Product product);

    List<Product> findByProductName(String name);

    boolean deleteProductById(Long id);

    //    FILTER
    PaginationProductResponse filterProducts(List<Double> price, String color, Category category, Set<Manufacture> manufactureSet, Pageable pageable,String[] colors);
    //    Search
    PaginationProductResponse searchProducts(String keyword, Pageable pageable);

    /*Count comment product*/
    Integer countCommentProduct(Long id);

    /* Rating product*/
    BigDecimal rating(Long id);

    void incrementViewCount(Long id);

}
