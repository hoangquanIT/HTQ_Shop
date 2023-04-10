package com.quanht.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShippingAddressRequest {

    private String name;
    private String email;
    private String phone;
    private String address;
    private String city;

}
