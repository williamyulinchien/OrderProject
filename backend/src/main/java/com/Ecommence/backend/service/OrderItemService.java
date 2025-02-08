package com.Ecommence.backend.service;

import com.Ecommence.backend.dto.OrderItemRequestDto;
import com.Ecommence.backend.dto.OrderItemResponseDto;

public interface OrderItemService {


        OrderItemResponseDto createOrderItem(Long orderId,Long userId, OrderItemRequestDto orderItemRequestDto);

        OrderItemResponseDto updateOrderItem(Long orderItemId, OrderItemRequestDto orderItemRequestDto);

        void deleteOrderItem(Long orderItemId);

        OrderItemResponseDto getOrderItemById(Long orderItemId);
}






