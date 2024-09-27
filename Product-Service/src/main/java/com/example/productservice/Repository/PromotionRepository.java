package com.example.productservice.Repository;

import com.example.productservice.Entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    // Có thể thêm các phương thức truy vấn tùy chỉnh ở đây
    List<Promotion> findByIdShop(Long idShop);

    @Query("SELECT p FROM Promotion p JOIN p.products prod " +
            "WHERE prod.id = :productId " +
            "AND p.startDate <= CURRENT_DATE " +
            "AND p.endDate >= CURRENT_DATE")
    List<Promotion> findActivePromotionsByProductId(@Param("productId") Long productId);
}