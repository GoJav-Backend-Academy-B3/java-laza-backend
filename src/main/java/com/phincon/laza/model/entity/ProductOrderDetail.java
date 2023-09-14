package com.phincon.laza.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "product_order_details")
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String productId;

    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String categoryName;

    private String brandName;

    private String size;

    private Integer price;

    private Integer quantity;

    private Integer totalPrice;

    @ManyToOne
    @JoinColumn(name="order_id", nullable=false)
    @JsonIgnore
    private Order order;
}
