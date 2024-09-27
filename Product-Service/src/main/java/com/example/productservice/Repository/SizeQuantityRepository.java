package com.example.productservice.Repository;

import com.example.productservice.Entity.SizeQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeQuantityRepository extends JpaRepository<SizeQuantity,Long> {
}
