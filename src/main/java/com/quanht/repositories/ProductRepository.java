package com.quanht.repositories;

import com.quanht.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    @Query(nativeQuery = true,

            value = "SELECT p.id, p.name, p.content, p.description, p.created_at, p.updated_at FROM product p " +
                    "INNER JOIN product_category pc ON p.id = pc.product_id INNER JOIN variant v ON p.id = v.product_id " +
                    "WHERE (:cateId IS NULL OR pc.category_id = :cateId) AND (:color IS NULL OR v.color LIKE :color) " +
                    "AND (:size IS NULL OR v.size LIKE :size) AND (:minPrice IS NULL OR v.price >= :minPrice)" +
                    "AND (:maxPrice IS NULL OR v.price <= :maxPrice) AND (:search IS NULL OR p.name LIKE CONCAT('%', :search ,'%'))" +
                    "GROUP BY p.id ORDER BY " +
                    "CASE WHEN :order = 'ASC' THEN MIN(v.price) END ASC," +
                    "CASE WHEN :order = 'DESC' THEN MIN(v.price) END DESC," +
                    "p.created_at DESC",

            countQuery = "SELECT COUNT(p.id) FROM product p INNER JOIN product_category pc ON p.id = pc.product_id " +
                    "INNER JOIN variant v ON p.id = v.product_id " +
                    "WHERE (:cateId IS NULL OR pc.category_id = :cateId) AND (:color IS NULL OR v.color LIKE :color) " +
                    "AND (:size IS NULL OR v.size LIKE :size) AND (:minPrice IS NULL OR v.price > :minPrice)" +
                    "AND (:maxPrice IS NULL OR v.price <= :maxPrice) AND (:search IS NULL OR p.name LIKE CONCAT('%', :search ,'%'))" +
                    "GROUP BY p.id ORDER BY " +
                    "CASE WHEN :order = 'ASC' THEN MIN(v.price) END ASC," +
                    "CASE WHEN :order = 'DESC' THEN MIN(v.price) END DESC," +
                    "p.created_at DESC")
    Page<Product> findByCategoryPage(@Param("cateId") Long cateId,
                                           @Param("color") String color,
                                           @Param("size") String size,
                                           @Param("minPrice") Double minPrice,
                                           @Param("maxPrice") Double maxPrice,
                                           @Param("order") String order,
                                           @Param("search") String search,
                                           Pageable pageable);

}
