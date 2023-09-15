package com.phincon.laza.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @NotNull
    private String fullAddress;

    @Column(nullable = false)
    @NotNull
    private String receiverName;

    @Column(nullable = false)
    @NotNull
    private String phoneNumber;

    @Column(nullable = false)
    @NotNull
    private String province;

    @Column(nullable = false)
    @NotNull
    private String cityType;

    @Column(nullable = false)
    @NotNull
    private String cityName;

    @Column(nullable = false)
    @NotNull
    private String postalCode;

    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Order order;
}
