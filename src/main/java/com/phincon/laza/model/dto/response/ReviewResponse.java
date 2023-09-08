package com.phincon.laza.model.dto.response;


import com.phincon.laza.model.entity.Product;
import com.phincon.laza.model.entity.Review;
import com.phincon.laza.model.entity.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewResponse {
    private Long id;
    private String comment;
    private float rating;
    private LocalDateTime createdAt;
    private User users;
    private Product products;

    public ReviewResponse(Review review) {
        this.id = review.getId();
        this.comment = review.getComment();
        this.rating = review.getRating();
        this.createdAt = review.getCreatedAt();
        this.users = review.getUser();
        this.products = review.getProduct();

    }

}
