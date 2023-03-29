package com.quanht.controller.admin;

import com.quanht.request.EmployeeRequest;
import com.quanht.request.EmployeeUpdateRequest;
import com.quanht.request.UpdateInfoRequest;
import com.quanht.request.UpdatePasswordRequest;
import com.quanht.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/ecommerce/api/v1/employee")
public class EmployeeController {

    private AccountService accountService;

    @Autowired
    public EmployeeController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("")
    public ResponseEntity<?> getEmployees(){
        return ResponseEntity.ok(accountService.getAccounts());
    }

    @PostMapping("")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeRequest employeeRequest){
        accountService.createEmployee(employeeRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id){
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PutMapping("")
    public ResponseEntity<?> updateEmployee(@RequestBody EmployeeUpdateRequest employee){
        accountService.updateEmployee(employee);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id){
        accountService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getEmployeeDetails(HttpServletRequest request){
        return ResponseEntity.ok(accountService.getDetail(request));
    }

    @PutMapping("/detail")
    public ResponseEntity<?> updateEmployeeDetails(@RequestBody UpdateInfoRequest request){
        return ResponseEntity.ok(accountService.updateInfo(request));
    }

    @PutMapping("/detail/password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest, HttpServletRequest request){
        return ResponseEntity.ok(accountService.updatePassword(updatePasswordRequest, request));
    }

}
