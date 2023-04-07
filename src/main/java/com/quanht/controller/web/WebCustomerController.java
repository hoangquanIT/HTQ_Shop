package com.quanht.controller.web;

import com.quanht.entities.Account;
import com.quanht.request.CustomerUpdateRequest;
import com.quanht.request.UpdateInfoRequest;
import com.quanht.request.UpdatePasswordRequest;
import com.quanht.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WebCustomerController {

    private AccountService accountService;

    @Autowired
    public WebCustomerController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/ecommerce/api/v1/client/account")
    public ResponseEntity<?> getCustomerInfo(HttpServletRequest request){
        return ResponseEntity.ok(accountService.getClientDetail(request));
    }

    @PutMapping("/ecommerce/api/v1/client/account")
    public ResponseEntity<?> updateCustomer(HttpServletRequest httpRequest,
                                            @RequestBody CustomerUpdateRequest request){
//        Account account = accountService.getClientDetail(httpRequest);
//        request.setId(account.getId());
        accountService.updateCustomer(request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/ecommerce/api/v1/client/account/password")
    public ResponseEntity<?> updateCustomerPassword(HttpServletRequest httpRequest,
                                            @RequestBody UpdatePasswordRequest request){
        return ResponseEntity.ok(accountService.updateClientPassword(request, httpRequest));
    }

    @GetMapping("/ecommerce/api/v1/client/account/cartNumbers")
    public ResponseEntity<?> getCustomerCart(HttpServletRequest request){
        return ResponseEntity.ok(accountService.getClientCartNumbers(request));
    }
}
