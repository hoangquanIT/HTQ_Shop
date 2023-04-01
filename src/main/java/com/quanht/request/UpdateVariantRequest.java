package com.quanht.request;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateVariantRequest {

    @NotEmpty(message = "Mã SKU không được để trống")
    private String sku;
    @NotNull(message = "Giá không được để trống")
    @Min(value = 0, message = "Nhập tối thiểu 0")
    private Double price;
    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Nhập tối thiểu 0")
    private Integer quantity;

}
