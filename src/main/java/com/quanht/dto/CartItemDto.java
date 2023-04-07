package com.quanht.dto;

import com.quanht.entities.Variant;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItemDto {

    private Long itemId;
    private String imageUrl;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Variant variant;
    private Double total;

}
