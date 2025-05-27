package com.sushantproject.ecommerce_mini_microservices.inventory_service.controller;

import com.sushantproject.ecommerce_mini_microservices.inventory_service.dto.ProductDto;
import com.sushantproject.ecommerce_mini_microservices.inventory_service.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/fetchOrders")
    public String fetchFromOrderService(HttpServletRequest httpServletRequest) {

        log.info(httpServletRequest.getHeader("x-custom-header"));
        ServiceInstance orderService = discoveryClient.getInstances("order-service").getFirst();
        return restClient.get()
                .uri(orderService.getUri() + "/orders/core/helloOrders")
                .retrieve()
                .body(String.class);
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

}