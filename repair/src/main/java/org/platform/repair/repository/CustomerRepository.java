package org.platform.repair.repository;

import org.platform.repair.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByIdAndShopId(
            Long id,
            Long shopId
    );

    Optional<Customer> findByNameAndShopId(
            String name,
            Long shopId
    );

    Optional<Customer> findByPhoneAndShopId(
            String phone,
            Long shopId
    );

    List<Customer> findByShopId(
            Long shopId
    );

    Long countByShopId(
            Long shopId
    );

    @Query("""
            SELECT c
            FROM Customer c
            WHERE c.shop.id = :shopId
            AND (
                LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))
                OR c.phone LIKE CONCAT('%', :query, '%')
            )
            """)
    List<Customer> search(Long shopId, String query);
}