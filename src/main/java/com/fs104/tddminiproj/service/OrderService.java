package com.fs104.tddminiproj.service;

import com.fs104.tddminiproj.model.Orders;
import com.fs104.tddminiproj.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
public class OrderService {
    private final OrderRepository repository;

    public OrderService(OrderRepository repository){
        this.repository = repository;
    }
    public Orders createNewOrder(Orders orders){
        return repository.save(orders);
    }
    public Orders findExistingOrder(Long id){
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Order not found with ID: " + id));
    }
    public List<Orders> listAllOrders(){
        return repository.findAll();
    }
    public Orders updateOrder(Long id, Orders orders){
        Orders existingOrder = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Order not found with ID: " + id));
        existingOrder.setCustomerName(orders.getCustomerName());
        existingOrder.setOrderDate(orders.getOrderDate());
        existingOrder.setShippingAddress(orders.getShippingAddress());
        existingOrder.setTotal(orders.getTotal());
        return repository.save(existingOrder);
    }
    public void deleteOrder(Long id){
        repository.deleteById(id);
    }
}
