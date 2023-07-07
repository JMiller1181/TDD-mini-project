package com.fs104.tddminiproj.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotEmpty(message = "Customer name is required")
    private String customerName;
    @NotNull(message = "Date is required")
    private LocalDate orderDate;
    @NotEmpty(message = "Shipping address is required")
    private String shippingAddress;
    @Positive(message = "Total must be positive")
    private Double total;

    public Orders(String customerName, LocalDate orderDate, String shippingAddress, Double total) {
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.shippingAddress = shippingAddress;
        this.total = total;
    }

    public Orders() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String toString(){
        return "Order ID: " + id +
                "\nCustomer Name: " + customerName +
                "\nOrder Date: " + orderDate +
                "\nShipping Address: " + shippingAddress +
                "\nOrder Total: " + total;
    }
}
