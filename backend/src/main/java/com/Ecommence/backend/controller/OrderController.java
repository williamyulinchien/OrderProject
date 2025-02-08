package com.Ecommence.backend.controller;

import com.Ecommence.backend.dto.OrderItemRequestDto;
import com.Ecommence.backend.dto.OrderResponseDto;
import com.Ecommence.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("api/")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDto>> getOrders() {
        List<OrderResponseDto> orders =orderService.getAllOrders();
        return ResponseEntity.ok(orders);}
    // 1. Create a Order
    @PostMapping("users/{userId}/orders")
    public ResponseEntity<OrderResponseDto> createOrder(
            @PathVariable Long userId,
            @RequestBody @Valid List<OrderItemRequestDto> orderItems) {
        OrderResponseDto createdOrder = orderService.createOrder(userId, orderItems);
        return ResponseEntity.ok(createdOrder);
    }
   // 2. get all orders from this User
    @GetMapping("users/{userId}/orders")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUser(@PathVariable Long userId) {
        List<OrderResponseDto> orders = orderService.getAllOrdersbyUserId(userId);
        return ResponseEntity.ok(orders);
    }
    // 3. get a specific order from this User
    @GetMapping("users/{userId}/orders/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(
            @PathVariable Long userId,
            @PathVariable Long orderId) {
        OrderResponseDto order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
    // 4. update a specific order from this User
    @PutMapping("users/{userId}/orders/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrder(
            @PathVariable Long userId,
            @PathVariable Long orderId,
            @RequestBody @Valid List<OrderItemRequestDto> updatedOrderItems) {
        OrderResponseDto updated = orderService.updateOrder(orderId, updatedOrderItems);
        return ResponseEntity.ok(updated);
    }

    // 5. Delete a specific order from this User
    @DeleteMapping("users/{userId}/orders/{orderId}")
    public ResponseEntity<String> deleteOrder(
            @PathVariable Long userId,
            @PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok("Order delete successfully!");
    }
}




