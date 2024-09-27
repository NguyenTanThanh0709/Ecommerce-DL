package com.example.userservice.repositoty;

import com.example.userservice.Entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop,Long> {
    Optional<Shop> findBySellerId(Long sellerId);
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Shop s WHERE s.seller.id = :sellerId")
    boolean existsBySellerId(@Param("sellerId") Long sellerId);

    List<Shop> findByDistrict(String district);

    @Query("SELECT s FROM Shop s WHERE LOWER(s.city) LIKE LOWER(CONCAT('%', :district, '%'))")
    List<Shop> findByDistrictContainingIgnoreCase(@Param("district") String district);

}
