package com.quanht.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderUpdateRequest {

    private String note;
    private String status;
    private String payment;
    private String fulfillment;

}
