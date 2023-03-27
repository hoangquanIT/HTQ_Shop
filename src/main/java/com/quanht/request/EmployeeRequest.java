package com.quanht.request;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmployeeRequest {

    private String name;
    private String email;
    private String phone;
    private String password;
    private List<Long> roles;

}
