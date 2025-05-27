package com.sushantproject.ecommerce_mini_microservices.inventory_service.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private Double price;
    private Integer stock;
}
