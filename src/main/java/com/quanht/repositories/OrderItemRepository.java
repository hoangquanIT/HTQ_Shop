package com.quanht.repositories;

import com.quanht.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM OrderItem oi WHERE oi.id = :id")
    void customDeleteById(Long id);

}
