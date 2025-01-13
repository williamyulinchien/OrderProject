package com.Ecommence.backend.repository;
import com.Ecommence.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByName(String name);
    boolean existsByName(String name);
}
