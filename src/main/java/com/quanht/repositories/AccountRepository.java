package com.quanht.repositories;

import com.quanht.dto.AccountDto;
import com.quanht.dto.CustomerDto;
import com.quanht.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository  extends JpaRepository<Account, Long> {

    @Query(nativeQuery = true, name = "getAccounts")
    List<AccountDto> findAccountDtos();

    Optional<Account> findByEmail(String email);

    Optional<Account> findByPhone(String phone);

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.name = :nameValue, a.phone = :phoneValue, a.updatedAt = :updateAtValue WHERE a.id = :idValue")
    void updateEmployee(@Param("idValue") Long id,
                        @Param("nameValue") String name,
                        @Param("phoneValue") String phone,
                        @Param("updateAtValue") LocalDateTime updateAt);

    @Query(nativeQuery = true, name = "getCustomers")
    List<CustomerDto> findCustomerDtos();

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.name = :nameValue, a.phone = :phoneValue, a.address = :addressValue, " +
            "a.cityCode = :cityCodeValue, a.updatedAt = :updateAtValue WHERE a.id = :idValue")
    void updateCustomer(@Param("idValue") Long id,
                        @Param("nameValue") String name,
                        @Param("phoneValue") String phone,
                        @Param("addressValue") String address,
                        @Param("cityCodeValue") Integer cityCode,
                        @Param("updateAtValue") LocalDateTime updateAt);
    

}
