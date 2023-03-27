package com.quanht.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmployeeUpdateRequest {

    private Long id;
    private String name;
    private String phone;

}
