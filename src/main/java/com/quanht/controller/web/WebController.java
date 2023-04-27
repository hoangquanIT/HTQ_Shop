package com.quanht.controller.web;

import com.quanht.security.ClientJwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/shop")
public class WebController {

    private ClientJwtUtils clientJwtUtils;

    @Autowired
    public WebController(ClientJwtUtils clientJwtUtils) {
        this.clientJwtUtils = clientJwtUtils;
    }

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
    public String getProductPage(@PathVariable String id){
        return "web/product";
    }

    @GetMapping("/cart")
    public String getCartPage(){
        return "web/cart";
    }

    @GetMapping("/account")
    public String getCustomerDetails(HttpServletRequest request){
        if (clientJwtUtils.getTokenFromCookie(request) != null) {
            return "web/customer-info";
        } else {
            return "redirect:login";
        }
    }

    @GetMapping("/orders")
    public String getOrders(HttpServletRequest request) {
        if (clientJwtUtils.getTokenFromCookie(request) != null) {
            return "web/orders";
        } else {
            return "redirect:login";
        }
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
    public String getLoginPage(HttpServletRequest request){
        if (clientJwtUtils.getTokenFromCookie(request) != null) {
            return "redirect:";
        } else {
            return "web/login";
        }
    }

    @GetMapping("/register")
    public String getRegisterPage(HttpServletRequest request){
        if (clientJwtUtils.getTokenFromCookie(request) != null) {
            return "redirect:";
        } else {
            return "web/register";
        }
    }

    @GetMapping("/forgotPassword")
    public String getForgottenPasswordPage(HttpServletRequest request){
        if (clientJwtUtils.getTokenFromCookie(request) != null) {
            return "redirect:";
        } else {
            return "web/forgotten-password";
        }
    }

}
