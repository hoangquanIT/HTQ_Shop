package com.quanht.controller.admin;

import com.quanht.request.ShippingAddressRequest;
import com.quanht.service.ShippingAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ecommerce/api/v1/order/shipping-address")
public class ShippingAddressController {

    private ShippingAddressService shippingAddressService;

    @Autowired
    public ShippingAddressController(ShippingAddressService shippingAddressService) {
        this.shippingAddressService = shippingAddressService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateShippingAddress(@PathVariable Long id, @RequestBody ShippingAddressRequest request) {
        return ResponseEntity.ok(shippingAddressService.updateShippingAddress(id, request));
    }

}
