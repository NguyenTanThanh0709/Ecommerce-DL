package com.example.purchaseservice.Repository;

import com.example.purchaseservice.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByCustomerIdAndStatus(Long customerId, String status);
    List<Order> findByShopIdAndStatus(Long shopId, String status);
    List<Order> findByStatus(String status);
}
