package com.Ecommence.backend.controller;

import com.Ecommence.backend.dto.OrderItemRequestDto;
import com.Ecommence.backend.dto.OrderItemResponseDto;
import com.Ecommence.backend.service.OrderItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/orders/{orderId}/item")
public class OrderItemController {
    //DI the dependency
    private final OrderItemService orderItemService;
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    // 1. Create a orderItem from a user based on an order
    @PostMapping
    public ResponseEntity<OrderItemResponseDto> createOrderItem(
            @PathVariable Long userId,
            @PathVariable Long orderId,
            @RequestBody@Valid OrderItemRequestDto orderItemRequestDto) {
        OrderItemResponseDto orderItemResponse = orderItemService.createOrderItem(orderId,userId,orderItemRequestDto);
        return new ResponseEntity<>(orderItemResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{orderItemId}")
    public ResponseEntity<OrderItemResponseDto> getOrderItemById(
            @PathVariable Long userId,
            @PathVariable Long orderId,
            @PathVariable Long orderItemId) {
        OrderItemResponseDto orderItemResponse = orderItemService.getOrderItemById(orderItemId);
        return new ResponseEntity<>(orderItemResponse, HttpStatus.OK);
    }



    // 3. Update a orderItem from a user based on an order
    @PutMapping("/{orderItemId}")
    public ResponseEntity<OrderItemResponseDto> updateOrderItem(
            @PathVariable Long userId,
            @PathVariable Long orderId,
            @PathVariable Long orderItemId,
            @RequestBody OrderItemRequestDto orderItemRequestDto) {
        OrderItemResponseDto orderItemResponse = orderItemService.updateOrderItem(orderItemId, orderItemRequestDto);
        return new ResponseEntity<>(orderItemResponse, HttpStatus.OK);
    }

    // 4. Delete a orderItem from a user based on an order
    @DeleteMapping("/{orderItemId}")
    public ResponseEntity<String> deleteOrderItem(
            @PathVariable Long userId,
            @PathVariable Long orderId,
            @PathVariable Long orderItemId) {
        orderItemService.deleteOrderItem(orderItemId);
        return ResponseEntity.ok("Item delete successfully!");
    }



}

