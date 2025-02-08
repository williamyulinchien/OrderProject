package com.Ecommence.backend.mapper;

import com.Ecommence.backend.dto.OrderItemRequestDto;
import com.Ecommence.backend.dto.OrderItemResponseDto;
import com.Ecommence.backend.entity.Order;
import com.Ecommence.backend.entity.OrderItem;
import com.Ecommence.backend.entity.Product;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemMapper {

    // OrderItem entity -> OrderItemResponseDto
    public static OrderItemResponseDto mapToOrderItemResponseDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        OrderItemResponseDto orderItemResponseDto =  new OrderItemResponseDto();
        orderItemResponseDto.setOrderItemId((orderItem.getId()));
        orderItemResponseDto .setProductId(orderItem.getProduct().getId());
        orderItemResponseDto.setProductName(orderItem.getProduct().getName());
        orderItemResponseDto .setPrice(orderItem.getPrice());
        orderItemResponseDto.setQuantity(orderItem.getQuantity());
        orderItemResponseDto .setSubtotal(orderItem.getSubtotal());
        return orderItemResponseDto;
    }

    // OrderItemRequestDto ->  OrderItem entity
    public static OrderItem mapToOrderItem(OrderItemRequestDto orderItemRequestDto, Order order, Product product) {
        if (orderItemRequestDto == null || order == null || product == null) {
            return null;
        }
        return new OrderItem(
                order,
                product,
                orderItemRequestDto.getQuantity(),
                product.getPrice()
        );
    }

    // List<OrderItem> entity ->  List<OrderItemResponseDto>
    public static List<OrderItemResponseDto> mapToOrderItemResponseDtoList(List<OrderItem> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            return List.of();
        }
        return orderItems.stream()
                .map(OrderItemMapper::mapToOrderItemResponseDto)
                .collect(Collectors.toList());
    }
}

