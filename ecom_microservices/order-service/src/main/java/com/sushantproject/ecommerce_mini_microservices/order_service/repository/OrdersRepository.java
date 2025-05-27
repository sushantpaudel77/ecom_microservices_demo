package com.sushantproject.ecommerce_mini_microservices.order_service.repository;

import com.sushantproject.ecommerce_mini_microservices.order_service.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
}
