package com.Ecommence.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private Long userId;
    private String status;
    private List<OrderItemRequestDto> orderItems;
}
