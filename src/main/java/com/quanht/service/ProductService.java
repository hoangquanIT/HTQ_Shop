package com.quanht.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quanht.entities.Category;
import com.quanht.entities.Image;
import com.quanht.entities.Product;
import com.quanht.exception.BadRequestException;
import com.quanht.exception.NotFoundException;
import com.quanht.repositories.CategoryRepository;
import com.quanht.repositories.ProductRepository;
import com.quanht.repositories.VariantRepository;
import com.quanht.request.ProductRequest;
import com.quanht.request.UpdateProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ProductService {

    private ProductRepository productRepository;

    private CategoryRepository categoryRepository;

    private VariantRepository variantRepository;

    private ImageService imageService;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository,
                          VariantRepository variantRepository, ImageService imageService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.variantRepository = variantRepository;
        this.imageService = imageService;
    }

    public List<Product> getProducts() {
        return  productRepository.findAll();
    }

    public Product getProductById(Long id){
        return productRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Không tìm thấy sản phẩm");
        });
    }

    public List<Product> getSimilarProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Transactional
    public Product createProduct(MultipartFile[] files, String product){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductRequest request = objectMapper.readValue(product, ProductRequest.class);
            List<Category> categories = new ArrayList<>();

            if (productRepository.findByName(request.getName()).isPresent()){
                throw new BadRequestException("Tên sản phẩm đã tồn tại");
            }

            request.getCategories().forEach(id -> categories.add(categoryRepository.findById(id).get()));

            Product newProduct = Product.builder()
                    .name(request.getName())
                    .content(request.getContent())
                    .description(request.getDescription())
                    .categories(categories)
                    .build();
            productRepository.save(newProduct);

            if(request.getVariants().size() > 0) {
                request.getVariants().forEach(variant -> {

                    if (variantRepository.findBySku(variant.getSku()).isPresent()){
                        throw new BadRequestException("Mã SKU đã tồn tại");
                    }

                    variant.setProduct(newProduct);
                    variantRepository.save(variant);
                });
            }

            saveMultipleImage(files, newProduct);

            return newProduct;
        } catch (RuntimeException | JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    public void saveMultipleImage(MultipartFile[] files, Product product){
        if(files.length > 0) {
            Arrays.stream(files).forEach(img -> {

                String url = imageService.uploadFile(img);
                Image image = Image.builder()
                        .url(url)
                        .product(product)
                        .build();
                imageService.saveToDB(image);
            });
        }
    }

    @Transactional
    public Product updateProduct(Long id, UpdateProductRequest request){
        Optional<Product> checkProduct = productRepository.findByName(request.getName());
        if (checkProduct.isPresent()){
            if (checkProduct.get().getId() != id) {
                throw new BadRequestException("Tên sản phẩm đã tồn tại");
            }
        }
        Product product = getProductById(id);
        product.setName(request.getName());
        product.setContent(request.getContent());
        product.setDescription(request.getDescription());

        if (request.getCategoryIds().size() > 0){
            List<Category> categories = new ArrayList<>();
            request.getCategoryIds()
                    .forEach(categoryId -> categories.add(categoryRepository.findById(categoryId).get()));
            product.setCategories(categories);
        } else {
            product.getCategories().clear();
        }

        return product;
    }

    @Transactional
    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }

}
