package com.phincon.laza.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "address_order_details")
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddressOrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String country;

    @Column(nullable = false)
    @NotNull
    private String city;

    @Column(nullable = false)
    @NotNull
    private String receiverName;

    @Column(nullable = false)
    @NotNull
    private String phoneNumber;

    @OneToOne(fetch = FetchType.EAGER)
    private Order order;
}
