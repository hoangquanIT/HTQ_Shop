package com.quanht.controller.admin;

import com.quanht.request.UpdateVariantRequest;
import com.quanht.request.VariantRequest;
import com.quanht.service.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class VariantController {

    private VariantService variantService;

    @Autowired
    public VariantController(VariantService variantService) {
        this.variantService = variantService;
    }

    @GetMapping("/ecommerce/api/v1/variant")
    public ResponseEntity<?> getVariants(){
        return ResponseEntity.ok(variantService.getVariants());
    }

    @GetMapping("/ecommerce/api/v1/variant/{id}")
    public ResponseEntity<?> getVariant(@PathVariable Long id){
        return ResponseEntity.ok(variantService.getVariant(id));
    }

    @PostMapping("/ecommerce/api/v1/product/{id}/variant")
    public ResponseEntity<?> createVariant(@PathVariable Long id, @RequestBody VariantRequest variantRequest){

        return ResponseEntity.ok(variantService.createVariant(id, variantRequest));
    }

    @PutMapping("/ecommerce/api/v1/variant/{id}")
    public ResponseEntity<?> updateVariant(@PathVariable Long id, @Valid @RequestBody UpdateVariantRequest request){
        variantService.updateVariant(id, request);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/ecommerce/api/v1/variant/{id}")
    public ResponseEntity<?> deleteVariant(@PathVariable Long id){
        variantService.deleteVariant(id);
        return ResponseEntity.noContent().build();
    }

}
