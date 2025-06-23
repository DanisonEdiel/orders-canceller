package com.example.ordercanceler.service;

import com.example.ordercanceler.domain.Order;
import com.example.ordercanceler.dto.CancelOrderRequest;
import com.example.ordercanceler.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCancelerService {
    private final OrderRepository orderRepository;

    @Transactional
    public Order cancelOrder(String orderId, CancelOrderRequest request) {
        log.info("Canceling order with ID: {}", orderId);
        log.debug("Searching for order with ID: {} in the database (checking both id and orderId)", orderId);
        
        Optional<Order> orderOpt = orderRepository.findByOrderIdOrId(orderId);
        if (orderOpt.isEmpty()) {
            log.error("Order not found with ID: {} (checked both id and orderId)", orderId);
            try {
                UUID uuid = UUID.fromString(orderId);
                Optional<Order> orderByPrimaryId = orderRepository.findById(uuid);
                if (orderByPrimaryId.isPresent()) {
                    log.info("Found order by primary ID: {}", orderId);
                    return cancelOrderInternal(orderByPrimaryId.get(), request);
                }
            } catch (IllegalArgumentException e) {
                log.debug("The provided orderId is not a valid UUID: {}", orderId);
            }
            throw new EntityNotFoundException("Order not found with ID: " + orderId);
        }
        
        Order order = orderOpt.get();
        log.info("Found order: ID={}, OrderID={}, UserID={}, Status={}", 
                order.getId(), order.getOrderId(), order.getUserId(), order.getStatus());
        
        return cancelOrderInternal(order, request);
    }

    private Order cancelOrderInternal(Order order, CancelOrderRequest request) {
        // Verificar si la orden ya está cancelada
        if ("cancelled".equals(order.getStatus())) {
            log.info("Order is already cancelled: {}", order.getOrderId());
            return order;
        }
        
        // Actualizar el estado de la orden a cancelled
        order.setStatus("cancelled");
        order.setCancelledAt(LocalDateTime.now());
        order.setCancellationReason(request.getCancellationReason());
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        log.info("Order successfully cancelled: {}", savedOrder.getOrderId());
        
        return savedOrder;
    }
}
