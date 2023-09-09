package com.phincon.laza.model.dto.response;


import com.phincon.laza.model.entity.Review;
import com.phincon.laza.model.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponse {
    private Long id;
    private String comment;
    private String fullname;
    private String image;
    private float rating;
    private LocalDateTime createdAt;

    public ReviewResponse(Review review) {
        this.id = review.getId();
        this.comment = review.getComment();
        this.rating = review.getRating();
        this.createdAt = review.getCreatedAt();
        User user = review.getUser();
        this.fullname = user.getName();
        this.image = user.getImageUrl();
    }

}
