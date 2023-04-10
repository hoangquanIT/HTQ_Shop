package com.quanht.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("")
    public String getAdminLogin(){
        return "admin/login";
    }

    @GetMapping("/dashboard")
    public String getDashboard(){
        return "admin/index";
    }

    @GetMapping("/unauthorized")
    public String getUnauthorizedPage(){
        return "admin/access-denied";
    }

    @GetMapping("/detail")
    public String getDetail(){
        return "admin/detail";
    }

    @GetMapping("/customers")
    public String getCustomers(){
        return "admin/accounts/customers";
    }

    @GetMapping("/customer/{id}")
    public String getCustomer(@PathVariable Long id){
        return "admin/accounts/customer";
    }

    @GetMapping("/employees")
    public String getEmployees(){
        return "admin/accounts/employees";
    }

    @GetMapping("/employee/{id}")
    public String getEmployee(@PathVariable Long id){
        return "admin/accounts/employee";
    }

    @GetMapping("/roles")
    public String getRoles(){
        return "admin/accounts/roles";
    }

    @GetMapping("/orders")
    public String getOrders(){
        return "admin/orders/orders";
    }

    @GetMapping("/order/{id}")
    public String getOrder(@PathVariable String id){
        return "admin/orders/order";
    }

    @GetMapping("/category")
    public String getCategories(){
        return "admin/products/categories";
    }

    @GetMapping("/products")
    public String getProducts(){
        return "admin/products/products";
    }

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable Long id){
        return "admin/products/product";
    }

    @GetMapping("/products/create")
    public String getProductsCreate(){
        return "admin/products/product-create";
    }

    @GetMapping("/products/variants")
    public String getVariants(){
        return "admin/products/variants";
    }

}
