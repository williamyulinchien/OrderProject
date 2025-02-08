package com.Ecommence.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="product_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<OrderItem> orderItems;

    private String productImageUrl = "https://example.com/default_product.png";
}