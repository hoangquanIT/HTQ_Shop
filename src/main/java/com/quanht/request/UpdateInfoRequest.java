package com.quanht.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateInfoRequest {

    private Long id;
    private String name;
    private String phone;

}
