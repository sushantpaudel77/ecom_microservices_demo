package com.sushantproject.ecommerce_mini_microservices.inventory_service.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
