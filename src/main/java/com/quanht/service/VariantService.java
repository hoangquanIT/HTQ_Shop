package com.quanht.service;

import com.quanht.entities.Variant;
import com.quanht.exception.BadRequestException;
import com.quanht.repositories.ProductRepository;
import com.quanht.repositories.VariantRepository;
import com.quanht.request.VariantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VariantService {

    private VariantRepository variantRepository;
    private ProductRepository productRepository;

    @Autowired
    public VariantService(VariantRepository variantRepository, ProductRepository productRepository) {
        this.variantRepository = variantRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Variant createVariant(Long id, VariantRequest variantRequest){
        if (variantRepository.findBySku(variantRequest.getSku()).isPresent()){
            throw new BadRequestException("Mã SKU đã tồn tại");
        }
        Variant variant = Variant.builder()
                .sku(variantRequest.getSku())
                .size(variantRequest.getSize())
                .color(variantRequest.getColor())
                .price(variantRequest.getPrice())
                .product(productRepository.findById(id).get())
                .build();
        return variantRepository.save(variant);
    }

    public void deleteVariant(Long id){
        variantRepository.deleteById(id);
    }
}
