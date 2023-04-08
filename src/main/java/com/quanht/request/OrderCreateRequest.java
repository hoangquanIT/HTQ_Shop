package com.quanht.request;

import com.quanht.entities.Account;
import com.quanht.entities.OrderItem;
import com.quanht.entities.ShippingAddress;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderCreateRequest {
    private String note;
    private Double total;
    private Long cartId;
    private List<OrderItem> orderItems;
    private ShippingAddress shippingAddress;
    private Account customer;
}
