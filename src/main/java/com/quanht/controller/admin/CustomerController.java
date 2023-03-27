package com.quanht.controller.admin;

import com.quanht.request.CustomerRequest;
import com.quanht.request.CustomerUpdateRequest;
import com.quanht.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ecommerce/api/v1/customer")
public class CustomerController {

    private AccountService accountService;

    @Autowired
    public CustomerController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("")
    public ResponseEntity<?> getCustomers(){
        return ResponseEntity.ok(accountService.getCustomers());
    }

    @PostMapping("")
    public ResponseEntity<?> createCustomer(@RequestBody CustomerRequest customerRequest){
        accountService.createCustomer(customerRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable Long id){
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PutMapping("")
    public ResponseEntity<?> updateCustomer(@RequestBody CustomerUpdateRequest customerUpdateRequest){
        accountService.updateCustomer(customerUpdateRequest);
        return ResponseEntity.noContent().build();
    }

}
