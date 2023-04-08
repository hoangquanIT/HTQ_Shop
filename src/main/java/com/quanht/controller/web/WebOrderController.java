package com.quanht.controller.web;

import com.quanht.request.OrderCreateRequest;
import com.quanht.service.CartService;
import com.quanht.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WebOrderController {

    private OrderService orderService;
    private CartService cartService;

    @Autowired
    public WebOrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @PostMapping("/ecommerce/api/v1/client/checkout")
    public ResponseEntity<?> createOrder(HttpServletRequest request, @RequestBody OrderCreateRequest orderRequest){
        cartService.updateCartNote(orderRequest.getCartId(), orderRequest.getNote());
        return ResponseEntity.ok(orderService.createClientOrder(request, orderRequest));
    }

}
