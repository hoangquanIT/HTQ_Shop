package com.quanht.service;

import com.quanht.entities.Category;
import com.quanht.entities.Product;
import com.quanht.exception.BadRequestException;
import com.quanht.exception.NotFoundException;
import com.quanht.repositories.CategoryRepository;
import com.quanht.repositories.ProductCategoryRepository;
import com.quanht.repositories.ProductRepository;
import com.quanht.request.CategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    private ProductRepository productRepository;
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository,
                           ProductCategoryRepository productCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    public List<Category> getCategories(){
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id){
        return categoryRepository.findById(id).orElseThrow(() -> {
           throw new NotFoundException("Không tìm thấy nhóm sản phẩm");
        });
    }

    @Transactional
    public Category createCategory(CategoryRequest categoryRequest){
        if(categoryRepository.findByNameContainingIgnoreCase(categoryRequest.getName()).isPresent()){
            throw new BadRequestException("Nhóm sản phẩm đã tồn tại");
        }
        Category category = Category.builder()
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .build();
        return categoryRepository.save(category);
    }

    @Transactional
    public void updateCategory(Long id, CategoryRequest categoryRequest){
        Optional<Category> category = categoryRepository.findByNameContainingIgnoreCase(categoryRequest.getName());
        if(category.isPresent()){
            if (category.get().getId() != id) {
                throw new BadRequestException("Nhóm sản phẩm đã tồn tại");
            }
        }
        categoryRepository.updateCategory(id, categoryRequest.getName(), categoryRequest.getDescription(), LocalDateTime.now());
    }

    @Transactional
    public void deleteCategory(Long id){
        productCategoryRepository.deleteByCategoryId(id);
        categoryRepository.deleteById(id);
    }

    // ================================= WEB =================================
    public Page<Product> getProductsPage(Long cateId, String color, String size, Double minPrice, Double maxPrice,
                                         String order, String search, Pageable pageable){
        return productRepository.findByCategoryPage(cateId, color, size, minPrice, maxPrice, order, search, pageable);
    }
}
