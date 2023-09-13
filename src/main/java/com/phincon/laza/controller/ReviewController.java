package com.phincon.laza.controller;


import com.phincon.laza.model.dto.request.ReviewRequest;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.dto.response.ReviewResponse;
import com.phincon.laza.model.dto.response.ReviewsResponse;
import com.phincon.laza.model.entity.Review;
import com.phincon.laza.security.userdetails.CurrentUser;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/review")

public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/{productId}")
    public ResponseEntity<ReviewsResponse> getReviewsByProductId(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getReviewsByProductId(productId);
        List<ReviewsResponse.ReviewItem> reviewItems = ReviewsResponse.createReviewItems(reviews);
        int totalReviews = reviews.size();
        float averageRating = reviewService.calculateAverageRating(reviewItems);

        ReviewsResponse response = new ReviewsResponse(HttpStatus.OK.value(), "Success");
        response.setReviewData(reviewItems, totalReviews, averageRating);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{productId}/add")
    public ResponseEntity<DataResponse<ReviewResponse>> createReview(@CurrentUser SysUserDetails ctx, @PathVariable Long productId, @RequestParam String orderId, @Valid @RequestBody ReviewRequest reviewRequest) throws Exception {
        Review createdReview = reviewService.save(ctx.getId(), orderId, productId, reviewRequest);
        ReviewResponse reviewResponse = new ReviewResponse(createdReview);
        DataResponse<ReviewResponse> response = new DataResponse<ReviewResponse>(
                HttpStatus.CREATED.value(),
                "Review Created Successfully",
                reviewResponse,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
