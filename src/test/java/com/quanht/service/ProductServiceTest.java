package com.quanht.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.quanht.entities.Product;
import com.quanht.exception.BadRequestException;
import com.quanht.exception.NotFoundException;
import com.quanht.repositories.CategoryRepository;
import com.quanht.repositories.ProductRepository;
import com.quanht.repositories.VariantRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ProductService.class})
@ExtendWith(SpringExtension.class)
class ProductServiceTest {
    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private ImageService imageService;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @MockBean
    private VariantRepository variantRepository;

    /**
     * Method under test: {@link ProductService#getProductById(Long)}
     */
    @Test
    void testGetProductById() {
        Product product = new Product();
        product.setCategories(new ArrayList<>());
        product.setContent("Not all who wander are lost");
        product.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        product.setDescription("The characteristics of someone or something");
        product.setId(1L);
        product.setImages(new ArrayList<>());
        product.setName("Name");
        product.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        product.setVariants(new ArrayList<>());
        Optional<Product> ofResult = Optional.of(product);
        when(productRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertSame(product, productService.getProductById(1L));
        verify(productRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ProductService#getProductById(Long)}
     */
    @Test
    void testGetProductById2() {
        when(productRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.getProductById(1L));
        verify(productRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link ProductService#getProductById(Long)}
     */
    @Test
    void testGetProductById3() {
        when(productRepository.findById(Mockito.<Long>any())).thenThrow(new BadRequestException("An error occurred"));
        assertThrows(BadRequestException.class, () -> productService.getProductById(1L));
        verify(productRepository).findById(Mockito.<Long>any());
    }
}

