package com.Ecommence.backend.service;

import com.Ecommence.backend.dto.OrderItemRequestDto;
import com.Ecommence.backend.dto.OrderResponseDto;
import com.Ecommence.backend.entity.Order;
import com.Ecommence.backend.entity.OrderItem;
import com.Ecommence.backend.entity.Product;
import com.Ecommence.backend.entity.User;
import com.Ecommence.backend.exception.OrderException;
import com.Ecommence.backend.exception.ProductException;
import com.Ecommence.backend.mapper.OrderMapper;
import com.Ecommence.backend.repository.OrderRepository;
import com.Ecommence.backend.repository.ProductRepository;
import com.Ecommence.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public OrderResponseDto createOrder(Long userId, List<OrderItemRequestDto> orderItemRequestDtos) {
        // check the existing user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new OrderException("User not found with ID: " + userId));

        // create a new order
        Order order = new Order();
        order.setUser(user);
        order.setStatus("Pending");

        // Add orderItem if there are
        for (OrderItemRequestDto requestDto : orderItemRequestDtos) {
            Product product = productRepository.findById(requestDto.getProductId())
                    .orElseThrow(() -> new ProductException("Product not found with ID: " + requestDto.getProductId()));

            OrderItem orderItem = new OrderItem(order, product, requestDto.getQuantity(), product.getPrice());
            order.getOrderItems().add(orderItem);
        }

       // calculate total price
        double totalPrice = order.getOrderItems().stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
        // update the total price
        order.setTotalPrice(totalPrice);
        // save to database
        Order savedOrder = orderRepository.save(order);
        // return to mapToOrderResponseDto
        return OrderMapper.mapToOrderResponseDto(savedOrder);
    }

    @Override
    public OrderResponseDto updateOrder(Long orderId, List<OrderItemRequestDto> orderItemRequestDtos) {
        // Check if there is product id
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found with ID: " + orderId));
        // clean al orderItem
        order.getOrderItems().clear();

        // add new orderItems
        for (OrderItemRequestDto requestDto : orderItemRequestDtos) {
            Product product = productRepository.findById(requestDto.getProductId())
                    .orElseThrow(() -> new ProductException("Product not found with ID: " + requestDto.getProductId()));

            OrderItem orderItem = new OrderItem(order, product, requestDto.getQuantity(), product.getPrice());
            order.getOrderItems().add(orderItem);
        }

        //calculate total price
        double totalPrice = order.getOrderItems().stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
        // update the total price
        order.setTotalPrice(totalPrice);
        // save to the database
        Order updatedOrder = orderRepository.save(order);
        // return to mapToOrderResponseDto
        return OrderMapper.mapToOrderResponseDto(updatedOrder);
    }
    // method : delete the order
    @Override
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found with ID: " + orderId));

        orderRepository.delete(order);

    }
    //method: get the specific Order
    @Override
    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found with ID: " + orderId));
        return OrderMapper.mapToOrderResponseDto(order);
    }

    @Override
    public List<OrderResponseDto> getAllOrdersbyUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new OrderException("User not found with ID: " + userId));

        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream()
                .map(OrderMapper::mapToOrderResponseDto)
                .collect(Collectors.toList());
    }}