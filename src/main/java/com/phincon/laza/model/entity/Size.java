package com.phincon.laza.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "sizes")
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String size;

    @ManyToMany
    @JoinTable(
            name = "size_products",
            joinColumns = @JoinColumn(name = "size_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;

    @OneToMany(mappedBy = "size", fetch = FetchType.LAZY)
    private List<Cart> carts;

    @JsonIgnore
    private Boolean isDeleted = false;

}
