package com.project.spring.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "images")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_image;
    private String url;
    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false)
    private Product product_image;
}
