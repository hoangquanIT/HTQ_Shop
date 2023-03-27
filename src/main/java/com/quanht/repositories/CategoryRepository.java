package com.quanht.repositories;

import com.quanht.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameContainingIgnoreCase(String name);

    @Modifying
    @Transactional
    @Query("Update Category c SET c.name = :nameValue, c.description = :desValue, c.updatedAt = :updatedAtValue WHERE c.id = :idValue")
    void updateCategory(@Param("idValue") Long id,
                        @Param("nameValue") String name,
                        @Param("desValue") String des,
                        @Param("updatedAtValue") LocalDateTime updatedAt);
}
