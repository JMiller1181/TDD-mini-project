package com.fs104.tddminiproj;

import com.fs104.tddminiproj.model.Order;
import com.fs104.tddminiproj.repository.OrderRepository;
import com.fs104.tddminiproj.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

@DataJpaTest
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = TddMiniProjApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
class TddMiniProjApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private OrderRepository repository;
    @Autowired
    private OrderService service;

    @Test
    void createOrderShouldSaveNewOrderToRepository(){
        LocalDate date = LocalDate.now();
        Order order = new Order("John", date, "123 Park Place", 2.99);
        service.createNewOrder(order);
        assert(repository.findAll()).contains(order);
    }

    @Test
    void contextLoads() {
    }

}
