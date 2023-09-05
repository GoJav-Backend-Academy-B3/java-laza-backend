package com.phincon.laza.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "product_order_histories")
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductOrderHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imageUrl;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    private String categoryName;

    private String brandName;

    private String size;

    private Integer price;

    private Integer quantity;

    private Integer total_price;

    @ManyToOne
    @JoinColumn(name="order_id", nullable=false)
    private Order order;
}
