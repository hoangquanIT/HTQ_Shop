package com.quanht.controller.web;

import com.quanht.request.CartRequest;
import com.quanht.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WebCartController {

    private CartService cartService;

    @Autowired
    public WebCartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/ecommerce/api/v1/client/cart/{id}")
    public ResponseEntity<?> getCart(@PathVariable Long id){
        return ResponseEntity.ok(cartService.getCartDto(id));
    }

    @PostMapping("/ecommerce/api/v1/client/cart")
    public ResponseEntity<?> addToCart(@RequestBody CartRequest request, HttpServletRequest httpRequest){
        return ResponseEntity.ok(cartService.addToCart(request, httpRequest));
    }

    @DeleteMapping("/ecommerce/api/v1/client/cart/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable Long id){
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/ecommerce/api/v1/client/cart/item/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long id){
        cartService.removeCartItem(id);
        return ResponseEntity.noContent().build();
    }

}
