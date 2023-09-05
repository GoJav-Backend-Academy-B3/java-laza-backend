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
@Table(name = "verification_tokens")
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    private LocalDateTime expiryDate;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
}
