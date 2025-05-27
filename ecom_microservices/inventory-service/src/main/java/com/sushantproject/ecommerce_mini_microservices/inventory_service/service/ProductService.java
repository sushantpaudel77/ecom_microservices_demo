package com.sushantproject.ecommerce_mini_microservices.inventory_service.service;

import com.sushantproject.ecommerce_mini_microservices.inventory_service.dto.ProductDto;
import com.sushantproject.ecommerce_mini_microservices.inventory_service.entity.Product;
import com.sushantproject.ecommerce_mini_microservices.inventory_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    public List<ProductDto> getAllInventory() {
        log.info("Fetching all inventory items");
        return productRepository.findAll()
                .stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
    }

    public ProductDto getProductById(Long productId) {
        log.info("Fetching product by with ID: {}", productId);
        Optional<Product> inventory = productRepository.findById(productId);
        return inventory.map(item -> modelMapper.map(item, ProductDto.class))
                .orElseThrow(() -> new RuntimeException("Product not found with the ID: " + productId));
    }
}
