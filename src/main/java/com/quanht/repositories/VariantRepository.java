package com.quanht.repositories;

import com.quanht.dto.VariantDto;
import com.quanht.entities.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VariantRepository extends JpaRepository<Variant, Long> {

    @Query(nativeQuery = true, name = "getVariants")
    List<VariantDto> findVariantDtos();

    Optional<Variant> findBySku(String sku);

}
