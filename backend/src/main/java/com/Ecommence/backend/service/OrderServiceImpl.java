package com.Ecommence.backend.service;

import com.Ecommence.backend.dto.OrderItemRequestDto;
import com.Ecommence.backend.dto.OrderItemResponseDto;
import com.Ecommence.backend.dto.OrderResponseDto;
import com.Ecommence.backend.entity.Order;
import com.Ecommence.backend.entity.OrderItem;
import com.Ecommence.backend.entity.Product;
import com.Ecommence.backend.entity.User;
import com.Ecommence.backend.exception.OrderException;
import com.Ecommence.backend.exception.ProductException;
import com.Ecommence.backend.mapper.OrderMapper;
import com.Ecommence.backend.repository.OrderItemRepository;
import com.Ecommence.backend.repository.OrderRepository;
import com.Ecommence.backend.repository.ProductRepository;
import com.Ecommence.backend.repository.UserRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private  final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemService orderItemService;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository,OrderItemService orderItemService,OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderItemService = orderItemService;
        this.orderItemRepository = orderItemRepository;
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
        Order savedOrder = orderRepository.save(order);

        double totalPrice = 0.0;
        for (OrderItemRequestDto requestDto : orderItemRequestDtos) {
            Product product = productRepository.findById(requestDto.getProductId())
                    .orElseThrow(() -> new ProductException("Product not found with ID: " + requestDto.getProductId()));


            OrderItemResponseDto orderItemResponseDto = orderItemService.createOrderItem(savedOrder.getId(), order.getId(), requestDto);
            totalPrice += orderItemResponseDto.getSubtotal();
        }


        // update the total price
        order.setTotalPrice(totalPrice);
        orderRepository.save(savedOrder);
        // return to mapToOrderResponseDto
        return OrderMapper.mapToOrderResponseDto(savedOrder);
    }

    @Override
    public OrderResponseDto updateOrder(Long orderId, List<OrderItemRequestDto> orderItemRequestDtos) {
        // Check if there is product id
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found with ID: " + orderId));
        // Use map to manage the orderItem
        Map<Long, OrderItem> currentOrderItemsMap = order.getOrderItems().stream()
                .collect(Collectors.toMap(item -> item.getProduct().getId(), item -> item));


        // add new orderItems form the request
        for (OrderItemRequestDto requestDto : orderItemRequestDtos) {
            Long productId = requestDto.getProductId();
            int newQuantity = requestDto.getQuantity();

            if (currentOrderItemsMap.containsKey(productId)) {

                OrderItem existingOrderItem = currentOrderItemsMap.get(productId);
                orderItemService.updateOrderItem(existingOrderItem.getId(), requestDto);
                currentOrderItemsMap.remove(productId);
            } else {

                orderItemService.createOrderItem(orderId,order.getUser().getId(), requestDto);
            }
        }
        //
        for (OrderItem remainingOrderItem : currentOrderItemsMap.values()) {
            orderItemService.deleteOrderItem(remainingOrderItem.getId());
        }
        //calculate total price
        Order updatedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found after update with ID: " + orderId));
        double totalPrice = updatedOrder.getOrderItems().stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
        // update the total price
        updatedOrder.setTotalPrice(totalPrice);
        // save to the database
        orderRepository.save(updatedOrder);
        // return to mapToOrderResponseDto
        return OrderMapper.mapToOrderResponseDto(updatedOrder);
    }
    // method : delete the order
    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        try{
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found with ID: " + orderId));


        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            product.setQuantity(product.getQuantity() + orderItem.getQuantity());
            productRepository.save(product);
        }

        // delete orderItem in batch  has better performance
        orderItemRepository.deleteAll(order.getOrderItems());

        // delete the order
        orderRepository.delete(order);
    }catch(OptimisticLockException e){
            throw new OrderException("The order item was updated or deleted by another transaction. Please try again.");

        }
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
    }
    @Override
    public List<OrderResponseDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderMapper::mapToOrderResponseDto)
                .collect(Collectors.toList());
    }


}