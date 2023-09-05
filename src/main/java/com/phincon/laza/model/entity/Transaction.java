package com.phincon.laza.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "payment_method_details")
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String referenceId;

    private Integer amount;

    private String description;

    private String type;

    private String provider;

    private String currency;

    private String transactionStatus;

    private Date createdAt;

    private Date updatedAt;

    @OneToOne
    @JoinColumn(name="order_id", nullable=false)
    private Order order;
}
