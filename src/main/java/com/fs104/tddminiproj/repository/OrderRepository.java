package com.fs104.tddminiproj.repository;

import com.fs104.tddminiproj.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
