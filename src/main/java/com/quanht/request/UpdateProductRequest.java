package com.quanht.request;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateProductRequest {

    private String name;
    private String content;
    private String description;
    private List<Long> categoryIds;

}
