package com.phincon.laza.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "verification_codes")
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4, nullable = false)
    private String code;

    private LocalDateTime expiryDate;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
}
