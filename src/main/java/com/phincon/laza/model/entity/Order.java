package com.phincon.laza.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
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

    private Date createdAt;

    private Date updatedAt;

    private LocalDateTime paidAt;

    private LocalDateTime expiryDate;

    private Integer shippingFee;

    private Integer adminFee;

    private String orderStatus;

    private boolean reviewed;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @JsonIgnore
    private User user;

    @JsonBackReference
    @OneToOne(mappedBy = "order")
    private Review review;

    @OneToOne(mappedBy = "order")
    private AddressOrderDetail addressOrderDetail;

    @OneToMany(mappedBy = "order")
    private List<ProductOrderDetail> productOrderDetails;

    @OneToOne(mappedBy = "order")
    private Transaction transaction;

    @OneToOne(mappedBy = "order")
    private PaymentDetail paymentDetail;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
