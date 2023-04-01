package com.quanht.service;

import com.quanht.dto.VariantDto;
import com.quanht.entities.Variant;
import com.quanht.exception.BadRequestException;
import com.quanht.exception.NotFoundException;
import com.quanht.repositories.ProductRepository;
import com.quanht.repositories.VariantRepository;
import com.quanht.request.UpdateVariantRequest;
import com.quanht.request.VariantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VariantService {

    private VariantRepository variantRepository;
    private ProductRepository productRepository;

    @Autowired
    public VariantService(VariantRepository variantRepository, ProductRepository productRepository) {
        this.variantRepository = variantRepository;
        this.productRepository = productRepository;
    }

    public List<VariantDto> getVariants() {
        return variantRepository.findVariantDtos();
    }

    public Variant getVariant(Long id){
        return variantRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Không tìm thấy phiên bản sản phẩm");
        });
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

    @Transactional
    public void updateVariant(Long id, UpdateVariantRequest request){
        Optional<Variant> variant = variantRepository.findBySku(request.getSku());
        if (variant.isPresent()){
            if (variant.get().getId() != id){
                throw new BadRequestException("Mã sku đã tồn tại");
            }
        }

        Variant newVariant = getVariant(id);
        newVariant.setSku(request.getSku());
        newVariant.setPrice(request.getPrice());
        newVariant.setQuantity(request.getQuantity());

    }

    @Transactional
    public void deleteVariant(Long id){
        variantRepository.deleteById(id);
    }
}
