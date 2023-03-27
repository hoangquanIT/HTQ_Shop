package com.quanht.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryRequest {

    @NotEmpty(message = "Tên nhóm sản phẩm không được để trống")
    private String name;

    private String description;


}
