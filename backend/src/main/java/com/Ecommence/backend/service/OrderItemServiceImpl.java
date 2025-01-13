package com.Ecommence.backend.service;

import com.Ecommence.backend.dto.OrderItemRequestDto;
import com.Ecommence.backend.dto.OrderItemResponseDto;
import com.Ecommence.backend.entity.Order;
import com.Ecommence.backend.entity.OrderItem;
import com.Ecommence.backend.entity.Product;
import com.Ecommence.backend.exception.OrderException;
import com.Ecommence.backend.exception.ProductException;
import com.Ecommence.backend.mapper.OrderItemMapper;
import com.Ecommence.backend.repository.OrderItemRepository;
import com.Ecommence.backend.repository.OrderRepository;
import com.Ecommence.backend.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public OrderItemResponseDto createOrderItem(Long orderId, OrderItemRequestDto orderItemRequestDto) {
        // check  if the order exist
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found with ID: " + orderId));
        // check  if the product exist
        Product product = productRepository.findById(orderItemRequestDto.getProductId())
                .orElseThrow(() -> new ProductException("Product not found with ID: " + orderItemRequestDto.getProductId()));
        // create a orderItem object
        OrderItem orderItem = new OrderItem(order, product, orderItemRequestDto.getQuantity(), product.getPrice());
        // save to database
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        order.getOrderItems().add(orderItem);
        // update the order total price
        double totalPrice = order.getOrderItems().stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
        order.setTotalPrice(totalPrice);
        // save to database
        orderRepository.save(order);
        return OrderItemMapper.mapToOrderItemResponseDto(orderItem);
    }


    @Override
    public OrderItemResponseDto updateOrderItem(Long orderItemId, OrderItemRequestDto orderItemRequestDto) {
        // check the orderItem exist
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new OrderException("OrderItem not found with ID: " + orderItemId));
        // check if there is a product to connect
         Product product = productRepository.findById(orderItemRequestDto.getProductId())
                .orElseThrow(() -> new ProductException("Product not found with ID: " + orderItemRequestDto.getProductId()));
         // update orderItem with product
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemRequestDto.getQuantity());
        orderItem.setPrice(product.getPrice());
        orderItem.setSubtotal(product.getPrice() * orderItemRequestDto.getQuantity());
        //save to database
        orderItemRepository.save(orderItem);

        // get the order
        Order order = orderItem.getOrder();
        double totalPrice = order.getOrderItems().stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
        // update the total price
        order.setTotalPrice(totalPrice);
        // save to database
        orderRepository.save(order);

        return OrderItemMapper.mapToOrderItemResponseDto(orderItem);
    }
    // Delete a specific orderItem
    @Override
    public void deleteOrderItem(Long orderItemId) {
        // check id there is the orderItem
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new OrderException("OrderItem not found with ID: " + orderItemId));

        Order order = orderItem.getOrder();
        order.getOrderItems().remove(orderItem);

        // update order's total price
        double totalPrice = order.getOrderItems().stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
        order.setTotalPrice(totalPrice);
        //save to the datebase
        orderRepository.save(order);
        orderItemRepository.delete(orderItem);
    }
    // Get the orderItem
    @Override
    public OrderItemResponseDto getOrderItemById(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new OrderException("OrderItem not found with ID: " + orderItemId));
        return OrderItemMapper.mapToOrderItemResponseDto(orderItem);
    }
}


