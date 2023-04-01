package com.quanht.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VariantDto {

    private Long id;
    private String sku;
    private String size;
    private String color;
    private Double price;
    private Integer quantity;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
