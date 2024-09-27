package com.example.userservice.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String type;
    private String city;
    private String district;
    private String ward;
    private String detailLocation;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User seller;
}
