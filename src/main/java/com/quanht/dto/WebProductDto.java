package com.quanht.dto;

import com.quanht.entities.Product;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WebProductDto {
    private Page<Product> productPage;
    private int currentPage;
    private String categoryName;

}
