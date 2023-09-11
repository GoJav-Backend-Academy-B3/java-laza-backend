package com.phincon.laza.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "credit_cards")
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "card number is mandatory")
    private String cardNumber;

    @NotBlank
    @Column(length = 2)
    @Size(max = 2)
    @NotNull(message = "expiry month is mandatory")
    private Integer expiryMonth;

    @NotBlank
    @Column(length = 2)
    @Size(max = 2)
    @NotNull(message = "expiry year is mandatory")
    private Integer expiryYear;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
}
