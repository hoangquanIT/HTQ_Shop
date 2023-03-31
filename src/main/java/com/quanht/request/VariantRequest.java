package com.quanht.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VariantRequest {

    private String sku;
    private String size;
    private String color;
    private Double price;

}
