package com.quanht.controller.admin;

import com.quanht.request.VariantRequest;
import com.quanht.service.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class VariantController {

    private VariantService variantService;

    @Autowired
    public VariantController(VariantService variantService) {
        this.variantService = variantService;
    }

    @PostMapping("/ecommerce/api/v1/product/{id}/variant")
    public ResponseEntity<?> createVariant(@PathVariable Long id, @RequestBody VariantRequest variantRequest){

        return ResponseEntity.ok(variantService.createVariant(id, variantRequest));
    }

    @DeleteMapping("/ecommerce/api/v1/variant/{id}")
    public ResponseEntity<?> deleteVariant(@PathVariable Long id){
        variantService.deleteVariant(id);
        return ResponseEntity.noContent().build();
    }

}
