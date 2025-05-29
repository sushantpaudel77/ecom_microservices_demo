package com.sushantproject.ecommerce_mini_microservices.order_service.service;

import com.sushantproject.ecommerce_mini_microservices.order_service.clients.InventoryOpenFeignClient;
import com.sushantproject.ecommerce_mini_microservices.order_service.dto.OrderRequestDto;
import com.sushantproject.ecommerce_mini_microservices.order_service.entity.OrderItem;
import com.sushantproject.ecommerce_mini_microservices.order_service.entity.OrderStatus;
import com.sushantproject.ecommerce_mini_microservices.order_service.entity.Orders;
import com.sushantproject.ecommerce_mini_microservices.order_service.repository.OrdersRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final ModelMapper modelMapper;
    private final InventoryOpenFeignClient inventoryOpenFeignClient;

    public List<OrderRequestDto> getAllOrders() {
        log.info("Fetching all orders");
        List<OrderRequestDto> mappedOrders = ordersRepository.findAll()
                .stream()
                .map(orders -> modelMapper.map(orders, OrderRequestDto.class))
                .toList();
        log.info("Fetched {} orders", mappedOrders.size());
        return mappedOrders;
    }

    public OrderRequestDto getOrderById(Long ordersId) {
        log.info("Fetching order with ID: {}", ordersId);
        Optional<Orders> optOrder = ordersRepository.findById(ordersId);

        return optOrder.map(orders -> {
            OrderRequestDto dto = modelMapper.map(orders, OrderRequestDto.class);
            log.info("Order found : {}", dto);
            return dto;
        }).orElseThrow(() -> {
            log.warn("Order not found with ID: {}", ordersId);
            return new RuntimeException("Order not found with the ID: " + ordersId);
        });
    }

//    @Retry(name = "inventoryRetry", fallbackMethod = "createOrderFallback")
    @CircuitBreaker(name = "inventoryCircuitBreaker", fallbackMethod = "createOrderFallback")
//    @RateLimiter(name = "inventoryRateLimiter", fallbackMethod = "createOrderFallback")
    public OrderRequestDto createOrder(OrderRequestDto orderRequestDto) {
        log.info("Calling the createOrder method");
        Double totalPrice = inventoryOpenFeignClient.reduceStocks(orderRequestDto);

        Orders orders = modelMapper.map(orderRequestDto, Orders.class);

        for (OrderItem orderItem : orders.getItems()) {
            orderItem.setOrders(orders);
        }

        orders.setTotalPrice(totalPrice);
        orders.setOrderStatus(OrderStatus.CONFIRMED);

        Orders savedOrder = ordersRepository.save(orders);

        return modelMapper.map(savedOrder, OrderRequestDto.class);
    }

    public OrderRequestDto createOrderFallback(OrderRequestDto orderRequestDto, Throwable throwable) {
        log.error("Fallback triggered for createOrder due to: {}", throwable.getMessage());
        return new OrderRequestDto();
    }
}
