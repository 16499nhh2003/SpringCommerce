package com.project.spring.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "pro_man")
@Data
public class ProductManufacture implements Serializable {

    @EmbeddedId
    private ProductManufactureID id;
    @ManyToOne
    @MapsId("product_id")
    @JoinColumn(name = "product_id")
    private Product product_id;

    @ManyToOne
    @MapsId("manufacture_id")
    @JoinColumn(name = "manufacture_id")
    private Manufacture manufacture_id;

}
