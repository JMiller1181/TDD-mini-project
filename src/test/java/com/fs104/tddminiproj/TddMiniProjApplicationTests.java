package com.fs104.tddminiproj;

import com.fs104.tddminiproj.model.Orders;
import com.fs104.tddminiproj.repository.OrderRepository;
import com.fs104.tddminiproj.service.OrderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = TddMiniProjApplication.class
)
@TestPropertySource(locations = "classpath:application.properties")
class TddMiniProjApplicationTests {

    @Autowired
    private OrderRepository repository;
    @Autowired
    private OrderService service;

    @BeforeEach
    void clearRepository(){
        LocalDate date = LocalDate.now();
        repository.saveAllAndFlush(List.of(
                new Orders("John", date, "123 Park Place", 2.99),
                new Orders("Jane", date, "1000 Main Street", 5.99)
        ));
    }
    //Testing create function
    @Test
    void createOrderShouldSaveNewOrderToRepository(){
        LocalDate date = LocalDate.now();
        Orders orders = new Orders("Jacob", date, "100 S. Tryon Street", 9.99);
        service.createNewOrder(orders);
        assertThat(repository.findAll()).hasSize(3);
    }
    //Testing the read function for finding a specific order
    @Test
    void readOrderShouldReturnSpecificOrder(){
        assertThat(service.findExistingOrder(1L).getCustomerName()).isEqualTo("John");
    }

    //Testing read function for all orders
    @Test
    void listAllOrdersShouldReturnAllOrders(){
        assertThat(service.listAllOrders()).hasSize(2);
    }
//Testing the update functionality
    @Test
    void updateOrderShouldChangeExistingOrder(){
        LocalDate date = LocalDate.now();
        Orders orders1 = new Orders("James", date, "800 West Avenue", 1.99);
        repository.save(orders1);
        service.updateOrder(1L,orders1);
        assertThat(repository.findById(1L).orElseThrow(NoSuchElementException::new).getCustomerName())
                .isEqualTo("James");
    }
    //testing order deletion functionality
    @Test
    void deleteOrderShouldDeleteExistingOrder(){
        service.deleteOrder(1L);
        assertThat(repository.findAll()).hasSize(1);
    }
    @Test
    void contextLoads() {
    }

}
