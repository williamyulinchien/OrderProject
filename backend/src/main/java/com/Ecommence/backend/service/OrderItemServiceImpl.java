package com.Ecommence.backend.service;

import com.Ecommence.backend.dto.OrderItemRequestDto;
import com.Ecommence.backend.dto.OrderItemResponseDto;
import com.Ecommence.backend.entity.Order;
import com.Ecommence.backend.entity.OrderItem;
import com.Ecommence.backend.entity.Product;
import com.Ecommence.backend.entity.User;
import com.Ecommence.backend.exception.OrderException;
import com.Ecommence.backend.exception.ProductException;
import com.Ecommence.backend.mapper.OrderItemMapper;
import com.Ecommence.backend.repository.OrderItemRepository;
import com.Ecommence.backend.repository.OrderRepository;
import com.Ecommence.backend.repository.ProductRepository;
import com.Ecommence.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public OrderItemResponseDto createOrderItem(Long orderId,Long userId, OrderItemRequestDto orderItemRequestDto) {

        // check  if the product exist
        Product product = productRepository.findById(orderItemRequestDto.getProductId())
                .orElseThrow(() -> new ProductException("Product not found with ID: " + orderItemRequestDto.getProductId()));
        // check if the quantity of product is enough
        if (product.getQuantity() < orderItemRequestDto.getQuantity()) {
            throw new ProductException("Insufficient stock for product ID: " + product.getId() + " product name:"+ product.getName());
        }
        // update the quantity of product after place the orderItem
        product.setQuantity(product.getQuantity() - orderItemRequestDto.getQuantity());
        productRepository.save(product);
        Order order;
        if (orderId != null) {
            order = orderRepository.findById(orderId).orElse(null);
        } else {
            order = null;
        }

        if (orderId == 0) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new OrderException("User not found with ID: " + userId));
            order = new Order();
            order.setUser(user);
            order.setStatus("Pending");
            order = orderRepository.save(order);
        }

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
        Product newProduct = productRepository.findById(orderItemRequestDto.getProductId())
                .orElseThrow(() -> new ProductException("Product not found with ID: " + orderItemRequestDto.getProductId()));
        int newQuantity = orderItemRequestDto.getQuantity();

        // get current orderItem amd quantity
        Product currentProduct = orderItem.getProduct();
        int currentQuantity = orderItem.getQuantity();




        // 计算数量差异
        int quantityDifference = newQuantity - currentQuantity;

        if (currentProduct.getId().equals(newProduct.getId())) {
            if (quantityDifference > 0) {
                // 需要增加的数量
                if (newProduct.getQuantity() < quantityDifference) {
                    throw new ProductException("Insufficient stock for product ID: " + newProduct.getId());
                }
                newProduct.setQuantity(newProduct.getQuantity() - quantityDifference);
            } else {
                // 需要减少的数量
                newProduct.setQuantity(newProduct.getQuantity() - quantityDifference); // 减去负值相当于加上正值
            }
        } else {
            // 如果更改了产品，恢复当前产品库存，减少新产品库存
            currentProduct.setQuantity(currentProduct.getQuantity() + currentQuantity);
            if (newProduct.getQuantity() < newQuantity) {
                throw new ProductException("Insufficient stock for product ID: " + currentProduct.getId() + " product name:"+ currentProduct.getName());
            }
            newProduct.setQuantity(newProduct.getQuantity() - newQuantity);
        }


        // update orderItem with product
        orderItem.setProduct(newProduct);
        orderItem.setQuantity(orderItemRequestDto.getQuantity());
        orderItem.setPrice(newProduct.getPrice());
        orderItem.setSubtotal(newProduct.getPrice() * orderItemRequestDto.getQuantity());
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
    @Transactional
    public void deleteOrderItem(Long orderItemId) {
        // check id there is the orderItem
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new OrderException("OrderItem not found with ID: " + orderItemId));
        // recover the quantity after the orderItem deleted
        Product product = orderItem.getProduct();
        product.setQuantity(product.getQuantity() + orderItem.getQuantity());
        productRepository.save(product);
        // remove the orderItem from the order
        Order order = orderItem.getOrder();
        order.getOrderItems().remove(orderItem);
        // update order's total price
        double totalPrice = order.getOrderItems().stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
        order.setTotalPrice(totalPrice);
        //save to the database
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


