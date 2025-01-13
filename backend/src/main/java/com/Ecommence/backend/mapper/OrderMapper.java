package com.Ecommence.backend.mapper;

import com.Ecommence.backend.dto.OrderRequestDto;
import com.Ecommence.backend.dto.OrderResponseDto;
import com.Ecommence.backend.dto.OrderSummaryDto;
import com.Ecommence.backend.entity.Order;

import java.util.stream.Collectors;

public class OrderMapper {

    // Order entity -> OrderResponseDto
    public static OrderResponseDto mapToOrderResponseDto(Order order) {
        if (order == null) {
            return null;
        }
        return new OrderResponseDto(
                order.getId(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getCreatedAt() != null ? order.getCreatedAt().toString() : null,
                order.getUpdatedAt() != null ? order.getUpdatedAt().toString() : null,
                order.getUser() != null ? order.getUser().getId() : null,
                order.getOrderItems().stream()
                        .map(OrderItemMapper::mapToOrderItemResponseDto)
                        .collect(Collectors.toList())
        );
    }

    // OrderRequestDto -> Order entity
    public static Order mapToOrder(OrderRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }
        Order order = new Order();
        order.setStatus(requestDto.getStatus() != null ? requestDto.getStatus() : "Pending");
        order.setTotalPrice(0.0); // 初始0, 後續可在Service計算
        return order;
    }

    // OrderSummary entity -> OrderSummaryDto
    public static OrderSummaryDto mapToOrderSummaryDto(Order order) {
        if (order == null) {
            return null;
        }
        return new OrderSummaryDto(
                order.getId(),
                order.getTotalPrice(),
                order.getStatus()
        );
    }
}
