package com.quanht.controller.admin;

import com.quanht.request.OrderUpdateRequest;
import com.quanht.service.OrderService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ecommerce/api/v1/order")
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("")
    public ResponseEntity<?> getOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getOrderByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomerId(customerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable String id, @RequestBody OrderUpdateRequest request) {
        orderService.updateOrder(id, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getStatisticsByMonth() {
        return ResponseEntity.ok(orderService.getTotalEachMonth());
    }

    @GetMapping("/new")
    public ResponseEntity<?> getNumberOfNewOrders() {
        return ResponseEntity.ok(orderService.countNewOrders());
    }

    @GetMapping("/month-total")
    public ResponseEntity<?> getTotalOrdersCurMonth() {
        return ResponseEntity.ok(orderService.countTotalOrdersCurMonth());
    }

}
