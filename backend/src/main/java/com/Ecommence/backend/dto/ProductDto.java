package com.Ecommence.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long productId;
    @NotBlank(message = "Product name cannot be blank.")
    private String productName;
    @NotBlank(message = "Product description cannot be blank.")
    private String description;
    @NotNull(message = "Price cannot be null.")
    @Min(value = 0, message = "Price must be greater or equal to 0.")
    private Double price;
    @NotNull(message = "Quantity cannot be null.")
    @Min(value = 0, message = "Quantity must be greater or equal to 0.")
    private Integer quantity;
    private String productImageUrl;
}
