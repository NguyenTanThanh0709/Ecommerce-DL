    package com.example.productservice.Entity;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.*;
    import lombok.*;

    @Entity
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class SizeQuantity {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private String size;
        private String color;
        private int quantity;

        @ManyToOne
        @JoinColumn(name = "product_id", nullable = false)
        @JsonIgnore
        private Product product;
    }
