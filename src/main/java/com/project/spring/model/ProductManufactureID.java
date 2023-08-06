package com.project.spring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ProductManufactureID {
    @Column(name = "product_id")
    private Long pid;

    @Column(name = "manufacture_id")
    private Long mid;
}
