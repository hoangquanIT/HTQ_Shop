package com.quanht.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateInfoRequest {

    private String name;
    private String email;
    private String phone;

}
