package com.phincon.laza.model.dto.response;

import com.phincon.laza.model.entity.Product;
import com.phincon.laza.model.entity.Review;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;


@Data
public class ReviewsResponse {
    private int status_code;
    private String message;
    private ReviewData data;

    public ReviewsResponse(int status_code, String message) {
        this.status_code = status_code;
        this.message = message;
        this.data = data;
    }
    @Data
    public static class ReviewItem {
        private Long id;
        private String comment;
        private String name;
        private String image;
        private float rating;
        private String createdAt;
    }
    @Data
    public static class ReviewData {
        private List<ReviewItem> reviews;
        private int total_reviews;
        private float average_rating;
    }
    public void setReviewData(List<ReviewItem> reviewItems, int totalReviews, float averageRating) {
        ReviewData reviewData = new ReviewData();
        reviewData.setReviews(reviewItems);
        reviewData.setTotal_reviews(totalReviews);
        reviewData.setAverage_rating(Math.round(averageRating * 10.0f) / 10.0f);
        this.data = reviewData;
    }

    public static List<ReviewItem> createReviewItems(List<Review> reviews) {
        return reviews.stream().map(review -> {
            ReviewItem reviewItem = new ReviewItem();
            reviewItem.setId(review.getId());
            reviewItem.setComment(review.getComment());
            reviewItem.setName(review.getUser().getName());
            reviewItem.setImage(review.getUser().getImageUrl());
            reviewItem.setRating(review.getRating());
            reviewItem.setCreatedAt(review.getCreatedAt().toString());
            return reviewItem;
        }).collect(Collectors.toList());
    }
}
