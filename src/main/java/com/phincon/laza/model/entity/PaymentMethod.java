package com.phincon.laza.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "payment_methods")
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    private String name;

    private String code;

    @NotBlank
    private String type;

    @NotBlank
    private String provider;

    private boolean isActive;

    @NotBlank
    private String logoUrl;

    @OneToMany(mappedBy = "paymentMethod")
    private List<Order> order;

}
