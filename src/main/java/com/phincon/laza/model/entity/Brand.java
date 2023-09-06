package com.phincon.laza.model.entity;

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
@Table(name = "brands")
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String logoUrl;

    private boolean isDeleted;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private List<Product> productList;
}
