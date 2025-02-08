package com.Ecommence.backend.service;

import com.Ecommence.backend.dto.OrderItemRequestDto;
import com.Ecommence.backend.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {

    OrderResponseDto createOrder(Long userId, List<OrderItemRequestDto> orderItems);

    OrderResponseDto updateOrder(Long orderId, List<OrderItemRequestDto> updatedOrderItems);

    void deleteOrder(Long orderId);

    OrderResponseDto getOrderById(Long orderId);

    List<OrderResponseDto> getAllOrdersbyUserId(Long userId);

    List<OrderResponseDto> getAllOrders();
}

