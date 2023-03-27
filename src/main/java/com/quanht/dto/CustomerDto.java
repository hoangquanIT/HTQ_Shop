package com.quanht.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomerDto {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Integer cityCode;
    private LocalDateTime createdAt;

}
