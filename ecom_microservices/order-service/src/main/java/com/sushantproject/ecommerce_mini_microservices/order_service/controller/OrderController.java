package com.sushantproject.ecommerce_mini_microservices.order_service.controller;

import com.sushantproject.ecommerce_mini_microservices.order_service.clients.InventoryOpenFeignClient;
import com.sushantproject.ecommerce_mini_microservices.order_service.dto.OrderRequestDto;
import com.sushantproject.ecommerce_mini_microservices.order_service.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/core")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrdersService ordersService;

    @GetMapping("/helloOrders")
    public String helloOrders() {
        return "Hello from Order Service";
    }

    @PostMapping("/create-order")
    public ResponseEntity<OrderRequestDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        OrderRequestDto orderRequestDtoS = ordersService.createOrder(orderRequestDto);
        return new ResponseEntity<>(orderRequestDtoS, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderRequestDto>> getAllOrders() {
        log.info("Received request to get all orders");
        List<OrderRequestDto> allOrders = ordersService.getAllOrders();
        log.info("Successfully retrieved {} orders", allOrders.size());
        return ResponseEntity.ok(allOrders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderRequestDto> getOrderById(@PathVariable Long orderId) {
        log.info("Received request to get order with id: {}", orderId);
        OrderRequestDto order = ordersService.getOrderById(orderId);
        log.info("Successfully retrieved order with id: {}", orderId);
        return ResponseEntity.ok(order);
    }
}
