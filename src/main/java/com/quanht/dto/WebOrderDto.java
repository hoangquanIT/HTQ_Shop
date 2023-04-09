package com.quanht.dto;

import com.quanht.entities.ShippingAddress;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WebOrderDto {
    private String orderCode;
    private List<CartItemDto> items;
    private ShippingAddress shippingAddress;

}
