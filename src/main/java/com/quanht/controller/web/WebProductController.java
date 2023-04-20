package com.quanht.controller.web;

import com.quanht.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebProductController {

    private ProductService productService;

    @Autowired
    public WebProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/ecommerce/api/v1/client/product/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));

    }

    @GetMapping("/ecommerce/api/v1/client/product/similar/{keyword}")
    public ResponseEntity<?> getSimilarProducts(@PathVariable String keyword) {
        return ResponseEntity.ok(productService.getSimilarProducts(keyword));
    }

}
