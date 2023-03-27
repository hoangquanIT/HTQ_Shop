package com.quanht.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AccountDto {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Integer cityCode;
    private String roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
