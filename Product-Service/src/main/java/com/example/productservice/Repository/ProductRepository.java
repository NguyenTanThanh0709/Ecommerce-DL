package com.example.productservice.Repository;

import com.example.productservice.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByIdShop(Long shopId);
    List<Product> findByIdIn(List<Long> ids);

    @Query("SELECT p FROM Product p " +
            "WHERE (:name IS NULL OR lower(p.name) LIKE %:name%) " +
            "AND (:idcategory IS NULL OR p.category.id = :idcategory) " +
            "AND (:price_min IS NULL OR p.price >= :price_min) " +
            "AND (:price_max IS NULL OR p.price <= :price_max) " +
            "AND (:rating_filter IS NULL OR p.rating >= :rating_filter) " +
            "AND p.isPublic = true " +
            "ORDER BY " +
            "CASE WHEN :sortBy = 'view' THEN p.view END DESC, " +
            "CASE WHEN :sortBy = 'sold' THEN p.sold END DESC, " +
            "CASE WHEN :sortBy = 'price' AND :order = 'asc' THEN p.price END ASC, " +
            "CASE WHEN :sortBy = 'price' AND :order = 'desc' THEN p.price END DESC, " +
            "p.id") // Thêm sắp xếp theo ID để đảm bảo thứ tự nhất quán
    Page<Product> findAllWithFiltersAndSorting(@Param("name") String name,
                                               @Param("idcategory") Long idcategory,
                                               @Param("price_min") Double price_min,
                                               @Param("price_max") Double price_max,
                                               @Param("sortBy") String sortBy,
                                               @Param("order") String order,
                                               @Param("rating_filter") Integer rating_filter,
                                               Pageable pageable);


}
