package com.project.spring.dto;

import com.project.spring.model.Category;
import com.project.spring.model.Manufacture;
import com.project.spring.model.ProductImage;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.Set;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String color;
    private String originalPicture;
    private double price;
    private String nameCategory;
    private Set<String> manufacture;
    private String information;
    private String description;
    private Long viewCount;

    public ProductDTO() {
        this.viewCount = 0L; // Set the default value to 0
    }
}
