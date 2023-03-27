package com.quanht.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomerUpdateRequest {

    private Long id;
    private String name;
    private String phone;
    private String address;
    private Integer cityCode;

}
