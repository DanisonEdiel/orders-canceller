package com.example.ordercanceler.controller;

import com.example.ordercanceler.domain.Order;
import com.example.ordercanceler.dto.CancelOrderRequest;
import com.example.ordercanceler.service.OrderCancelerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderCancelerController {
    private final OrderCancelerService orderCancelerService;

    @PutMapping("/{orderId}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable String orderId,
            @Valid @RequestBody CancelOrderRequest request
    ) {
        log.info("Received request to cancel order with ID: {}", orderId);
        Order cancelledOrder = orderCancelerService.cancelOrder(orderId, request);
        return ResponseEntity.ok(cancelledOrder);
    }
}
