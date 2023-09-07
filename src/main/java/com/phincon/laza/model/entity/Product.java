package com.phincon.laza.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Table(name = "products")
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;

    private Integer price;

    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
    private List<Size> sizes;

    @ManyToOne
    @JoinColumn(name="brand_id", nullable=false)
    @JsonManagedReference
    private Brand brand;

    @ManyToOne
    @JoinColumn(name="category_id", nullable=false)
    @JsonManagedReference
    private Category category;

    @ManyToMany(mappedBy = "wishlistProducts", fetch = FetchType.LAZY)
    private List<User> wishlistBy;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Review> reviewList;

}

