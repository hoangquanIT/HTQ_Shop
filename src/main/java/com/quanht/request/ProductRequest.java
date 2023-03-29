package com.quanht.request;

import com.quanht.entities.Variant;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductRequest {

    private String name;
    private String content;
    private String description;
    private List<Long> categories;
    private List<Variant> variants;

}
