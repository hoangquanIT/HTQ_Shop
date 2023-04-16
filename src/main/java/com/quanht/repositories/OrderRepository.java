package com.quanht.repositories;

import com.quanht.dto.StatisticsDto;
import com.quanht.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    Optional<Order> findByCartId(Long cartId);

    List<Order> findByAccountId(Long accountId);


    @Query(nativeQuery = true, name = "statistics")
    List<StatisticsDto> getTotalSumEachMonth();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'NEW'")
    Long countNewOrder();

    @Query("SELECT COUNT(o) FROM Order o WHERE MONTH(o.updatedAt) = MONTH(CURDATE())")
    Long countTotalOrderCurrentMonth();

}
