package com.quanht.service;

import com.quanht.entities.Category;
import com.quanht.exception.BadRequestException;
import com.quanht.repositories.CategoryRepository;
import com.quanht.request.CategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getCategories(){
        return categoryRepository.findAll();
    }

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

    public void updateCategory(Long id, CategoryRequest categoryRequest){
        Optional<Category> category = categoryRepository.findByNameContainingIgnoreCase(categoryRequest.getName());
        if(category.isPresent()){
            if (category.get().getId() != id) {
                throw new BadRequestException("Nhóm sản phẩm đã tồn tại");
            }
        }
        categoryRepository.updateCategory(id, categoryRequest.getName(), categoryRequest.getDescription(), LocalDateTime.now());
    }

    public void deleteCategory(Long id){
        categoryRepository.deleteById(id);
    }
}
