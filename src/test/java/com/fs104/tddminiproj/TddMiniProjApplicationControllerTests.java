package com.fs104.tddminiproj;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fs104.tddminiproj.controller.OrderController;
import com.fs104.tddminiproj.model.Orders;
import com.fs104.tddminiproj.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@WebMvcTest(OrderController.class)
@TestPropertySource(locations = "classpath:application.properties")
public class TddMiniProjApplicationControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    void testCreateOrderRequest() throws Exception{
        LocalDate date = LocalDate.now();
        Orders order = new Orders("John", date, "123 Main Street", 12.99);
        mockMvc.perform(MockMvcRequestBuilders.post("/orders/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    @Test
    void testReadingAllOrdersRequest() throws Exception{
        LocalDate date = LocalDate.now();
        Orders order = new Orders("John", date, "123 Main Street", 12.99);
        List<Orders> ordersList = List.of(order);
        //Mocks the return of the method preventing the list from being empty
        when(orderService.listAllOrders()).thenReturn(ordersList);
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/read")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].orderDate").value(date.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].shippingAddress").value("123 Main Street"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].total").value(12.99));
    }
    @Test
    void testReadingOrderByIDRequest() throws Exception {
        Long orderId = 1L;
        LocalDate date = LocalDate.now();
        Orders order = new Orders("John", date, "123 Main Street", 12.99);
        order.setId(orderId);
        when(orderService.findExistingOrder(1L)).thenReturn(order);
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/read/{id}",orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(orderId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderDate").value(date.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shippingAddress").value("123 Main Street"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(12.99));
    }
    @Test
    void testUpdatingExistingOrderRequest() throws Exception{
        Long orderId = 1L;
        LocalDate date = LocalDate.now();
        Orders order = new Orders("John", date, "123 Main Street", 12.99);
        Orders newOrder = new Orders("Jane", date, "100 Main Street", 10.99);
        order.setId(orderId);
        newOrder.setId(orderId);
        when(orderService.updateOrder(orderId, newOrder)).thenReturn(newOrder);
        when(orderService.findExistingOrder(orderId)).thenReturn(newOrder);
        mockMvc.perform(MockMvcRequestBuilders.put("/orders/update/{id}",orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(orderId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("Jane"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderDate").value(date.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shippingAddress").value("100 Main Street"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(10.99));
    }
    @Test
    void testDeleteOrderRequest() throws Exception{
        Long orderID = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/orders/delete/{id}", orderID))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    @Test
    void testThatBadValuesCauseAnErrorForCreation() throws Exception{
        Orders order = new Orders("", null, "", -12.99);
        mockMvc.perform(MockMvcRequestBuilders.post("/orders/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void testThatBadValuesCauseAnErrorForUpdate() throws Exception{
        Long orderID = 1L;
        Orders newOrder = new Orders("", null, "", -12.99);
        newOrder.setId(orderID);
        mockMvc.perform(MockMvcRequestBuilders.put("/orders/update/{id}", orderID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void testThatReadingAnInvalidIDReturnsError() throws Exception{
        Long orderID = 1L;
        when(orderService.findExistingOrder(orderID)).thenThrow(new NoSuchElementException("Order not found with ID: " + orderID));
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/read/{id}", orderID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
    @Test
    void testThatUpdatingNonExistingOrderReturnsError() throws Exception{
        Long orderID = 1L;
        Orders order = new Orders("John", LocalDate.now(), "123 Main Street", 12.99);
        when(orderService.findExistingOrder(orderID)).thenThrow(new NoSuchElementException("Order not found with ID: " + orderID));
        mockMvc.perform(MockMvcRequestBuilders.put("/orders/update/{id}", orderID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
    @Test
    void testThatDeletingNonExistingOrderReturnsError() throws Exception{
        Long orderID = 1L;
        doThrow(new NoSuchElementException("Order not found with ID: " + orderID))
                .when(orderService).deleteOrder(orderID);
        mockMvc.perform(MockMvcRequestBuilders.delete("/orders/delete/{id}", orderID))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}
