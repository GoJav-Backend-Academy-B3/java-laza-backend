package com.phincon.laza.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Addresses")
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String provinceName;

    @Column(nullable = false)
    private String cityName;

    @Column(columnDefinition = "TEXT")
    private String fullAddress;

    private String receiverName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private boolean isPrimary;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @JsonBackReference
    private User user;

    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    private void onCreate() {
        createdAt = new Date();
        updatedAt = createdAt;
    }

    @PreUpdate
    private void onUpdate() {
        updatedAt = new Date();
    }
}
