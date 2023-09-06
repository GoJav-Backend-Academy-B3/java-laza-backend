package com.phincon.laza.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter @ToString
@Table(name = "orders")
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Order {

    @Id
    private String id;

    private Integer amount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime paidAt;

    private LocalDateTime expiryDate;

    private Integer shippingFee;

    private Integer adminFee;

    private String orderStatus;

//    @ManyToOne
//    @JoinColumn(name="address_id", nullable=false)
//    private Address address;

    @ManyToOne
    @JoinColumn(name="payment_method_id", nullable=false)
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "order")
    private List<ProductOrderDetail> productOrderDetails;

    @OneToOne(mappedBy = "order")
    private Transaction transaction;

    @OneToOne(mappedBy = "order")
    @JoinColumn(name="payment_detail_id", nullable=false)
    private PaymentDetail paymentDetail;
}
