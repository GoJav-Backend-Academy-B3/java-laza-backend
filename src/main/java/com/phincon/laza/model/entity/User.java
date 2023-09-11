package com.phincon.laza.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String username;

    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    @Column(nullable = false)
    private boolean isVerified = false;

    @Enumerated(EnumType.STRING)
    private EProvider provider;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @ManyToMany
    @JoinTable(
            name = "wishlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    @JsonBackReference
    private List<Product> wishlistProducts;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Address> addressList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<CreditCard> creditCardList;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<Review> reviewList;

    @OneToMany(mappedBy = "user")
    private List<VerificationCode> verificationCodeList;

    @OneToMany(mappedBy = "user")
    private List<VerificationToken> verificationTokenList;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Order> orders;

    public void addWishlist(Product product) {
        this.wishlistProducts.add(product);
        product.getWishlistBy().add(this);
    }

    public void removeProductWishlist(long productId) {
        Product product = this.wishlistProducts.stream().filter(t -> t.getId() == productId).findFirst().orElse(null);
        if (product != null) {
            this.wishlistProducts.remove(product);
            product.getWishlistBy().remove(this);
        }
    }

}
