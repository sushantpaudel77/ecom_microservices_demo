package com.sushantproject.ecommerce_mini_microservices.order_service.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Order, Long> {
}
