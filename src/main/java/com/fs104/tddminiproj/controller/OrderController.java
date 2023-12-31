package com.fs104.tddminiproj.controller;

import com.fs104.tddminiproj.model.Orders;
import com.fs104.tddminiproj.service.OrderService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<Orders> createOrder(@Valid @RequestBody Orders orders) {
            try {
                return new ResponseEntity<>(orderService.createNewOrder(orders), HttpStatus.CREATED);
            } catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }
    @GetMapping("/read")
    public ResponseEntity<List<Orders>> listAllOrders(){
        try {
            List<Orders> listOfOrders = orderService.listAllOrders();
            if (listOfOrders.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(listOfOrders, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/read/{id}")
    public ResponseEntity<Orders> readOrder(@PathVariable("id") Long id){
        try {
            return new ResponseEntity<>(orderService.findExistingOrder(id), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Orders> updateOrder(@PathVariable("id") Long id, @Valid @RequestBody Orders newOrder){
        try {
            orderService.updateOrder(id, newOrder);
            return new ResponseEntity<>(orderService.findExistingOrder(id), HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable("id") Long id){
        try {
            orderService.deleteOrder(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
