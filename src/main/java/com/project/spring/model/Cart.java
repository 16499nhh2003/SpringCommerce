package com.project.spring.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Cart{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AppUser user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @Column(columnDefinition = "double default 0")
    private Double total;

    public Double getCost(){
        double sum = 0.0;
        for(CartItem cartItem : cartItems){
            sum += cartItem.getProduct().getPrice()*cartItem.getQuantity();
        }
        return sum;
    }
}
