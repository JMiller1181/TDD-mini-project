package com.fs104.tddminiproj.service;

import com.fs104.tddminiproj.model.Order;
import com.fs104.tddminiproj.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderService {
    private OrderRepository repository;

    public OrderService(OrderRepository repository){
        this.repository = repository;
    }
    public void createNewOrder(Order order){

    }
}
