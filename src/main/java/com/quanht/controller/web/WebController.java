package com.quanht.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/shop")
public class WebController {

    // -------------------------------- SHOP --------------------------------
    @GetMapping("")
    public String getHomePage(){
        return "web/index";
    }

    @GetMapping("/category")
    public String getCategoryPage(){
        return "web/category";
    }

    @GetMapping("/product/{id}")
    public String getProductPage(@PathVariable Long id){
        return "web/product";
    }

    @GetMapping("/cart")
    public String getCartPage(){
        return "web/cart";
    }

    @GetMapping("/account")
    public String getCustomerDetails(){
        return "web/customer-info";
    }

    @GetMapping("/orders")
    public String getOrders() {
        return "web/orders";
    }

    @GetMapping("/orders/{id}")
    public String getOrders(@PathVariable String id) {
        return "web/order";
    }

    @GetMapping("/unauthorized")
    public String getUnauthorizedPage(){
        return "admin/access-denied";
    }

    // -------------------------------- PAYMENTS --------------------------------
    @GetMapping("/checkout")
    public String getCheckoutPage(){
        return "web/checkout";
    }

    @GetMapping("/checkout/payment")
    public String getCheckoutPaymentPage(){
        return "web/checkout-payment";
    }

    @GetMapping("/checkout/shipping")
    public String getCheckoutShippingPage(){
        return "web/checkout-shipping";
    }

    @GetMapping("/checkout/order/{id}")
    public String getOrderInfoAfterPayment(@PathVariable String id) {
        return "web/order-info";
    }


    // -------------------------------- LOGIN/REGISTER/FORGOTTEN --------------------------------
    @GetMapping("/login")
    public String getLoginPage(){
        return "web/login";
    }

    @GetMapping("/register")
    public String getRegisterPage(){
        return "web/register";
    }

    @GetMapping("/forgotPassword")
    public String getForgottenPasswordPage(){
        return "web/forgotten-password";
    }

}
