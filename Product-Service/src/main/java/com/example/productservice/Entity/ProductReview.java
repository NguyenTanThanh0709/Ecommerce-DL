package com.example.productservice.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReview {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long idCustomer;
    private int rating;
    private String comment;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product; // Added reference to Product

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
