package com.quanht.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StatisticsDto {

    private Integer months;
    private Double total;

}
