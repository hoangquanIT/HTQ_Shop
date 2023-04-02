package com.quanht.controller.web;

import com.quanht.dto.WebProductDto;
import com.quanht.entities.Product;
import com.quanht.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/ecommerce/api/v1/client/category")
public class WebCategoryController {

    private CategoryService categoryService;

    @Autowired
    public WebCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public ResponseEntity<?> getCategories(){
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @GetMapping("/products")
    public ResponseEntity<?> getProductByCategory(@RequestParam(value = "category", required = false) Long cateId,
                                                  @RequestParam(value = "color", required = false) String color,
                                                  @RequestParam(value = "size", required = false) String size,
                                                  @RequestParam(value = "min", required = false) Double minPrice,
                                                  @RequestParam(value = "max", required = false) Double maxPrice,
                                                  @RequestParam(value = "sort", required = false) String sort,
                                                  @RequestParam(value = "search", required = false) String search,
                                                  @RequestParam(value = "page", defaultValue = "1") int currentPage,
                                                  @RequestParam(value = "limit", defaultValue = "9") int pageSize){
        Page<Product> productPage = null;
        if (sort == null) {
             productPage = categoryService.getProductsPage(cateId, color, size, minPrice, maxPrice, null, search,
                    PageRequest.of(currentPage - 1, pageSize));
        } else {
            productPage = categoryService.getProductsPage(cateId, color, size, minPrice, maxPrice, sort.toUpperCase(), search,
                    PageRequest.of(currentPage - 1, pageSize));
        }
        String categoryName = "Tất cả sản phẩm";
        if (cateId != null){
            categoryName = categoryService.getCategoryById(cateId).getName();
        }
        WebProductDto productDto = WebProductDto.builder().
                productPage(productPage)
                .currentPage(currentPage)
                .categoryName(categoryName)
                .build();
        return ResponseEntity.ok(productDto);
    }

}
