package com.quanht.controller.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
