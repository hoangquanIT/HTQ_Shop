package com.quanht.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomerRequest {

    private String name;
    private String email;
    private String phone;
    private String password;
    private String address;
    private Integer cityCode;

}
