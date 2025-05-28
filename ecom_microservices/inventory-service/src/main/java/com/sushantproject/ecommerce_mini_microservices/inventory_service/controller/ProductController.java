package com.sushantproject.ecommerce_mini_microservices.inventory_service.controller;

import com.sushantproject.ecommerce_mini_microservices.inventory_service.client.OrdersFeignClient;
import com.sushantproject.ecommerce_mini_microservices.inventory_service.dto.OrderRequestDto;
import com.sushantproject.ecommerce_mini_microservices.inventory_service.dto.ProductDto;
import com.sushantproject.ecommerce_mini_microservices.inventory_service.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping(path = "/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final DiscoveryClient discoveryClient;
    private final RestClient restClient;
    private final OrdersFeignClient ordersFeignClient;

    @GetMapping("/fetchOrders")
    public String fetchFromOrderService(HttpServletRequest httpServletRequest) {

        log.info(httpServletRequest.getHeader("x-custom-header"));

        return ordersFeignClient.helloOrders();
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllInventory() {
        log.info("Received request to get all inventory items");
        List<ProductDto> allInventory = productService.getAllInventory();
        log.info("Successfully retrieved {} inventory items", allInventory.size());
        return ResponseEntity.ok(allInventory);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
        log.info("Received request to get product with id: {}", productId);
        ProductDto productById = productService.getProductById(productId);
        log.info("Successfully retrieved product with id: {}", productId);
        return ResponseEntity.ok(productById);
    }

    @PutMapping("/reduce-stocks")
    public ResponseEntity<Double> reduceStocks(@RequestBody OrderRequestDto orderRequestDto) {
       Double totalPrice = productService.reduceStocks(orderRequestDto);
       return ResponseEntity.ok(totalPrice);
    }
}
