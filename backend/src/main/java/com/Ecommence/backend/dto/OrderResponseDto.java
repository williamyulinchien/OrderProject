package com.Ecommence.backend.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
        "orderId",
        "totalPrice",
        "status",
        "createdAt",
        "updatedAt",
        "userId",
        "orderItems"
})
public class OrderResponseDto {
    private Long OrderId;
    private Double totalPrice;
    private String status;
    private String createdAt;
    private String updatedAt;
    private Long userId;
    private List<OrderItemResponseDto> orderItems;
}
