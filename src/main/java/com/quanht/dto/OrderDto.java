package com.quanht.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDto {

    private String id;
    private String customerName;
    private LocalDateTime createdAt;
    private Double total;
    private String status;
    private String payment;
    private String fulfillment;

}
