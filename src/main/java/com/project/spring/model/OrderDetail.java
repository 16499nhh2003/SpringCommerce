package com.project.spring.model;


import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
@Table(name = "orderdetails")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Product product;
    @ManyToOne
    private Order order;
    private int quantity;
}
