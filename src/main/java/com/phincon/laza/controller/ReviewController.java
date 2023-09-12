package com.phincon.laza.controller;


import com.phincon.laza.model.dto.request.ReviewRequest;
import com.phincon.laza.model.dto.response.CategoryResponse;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.dto.response.ReviewResponse;
import com.phincon.laza.model.dto.response.ReviewsResponse;
import com.phincon.laza.model.entity.Review;
import com.phincon.laza.security.userdetails.CurrentUser;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

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

    @PostMapping("/{productId}/reviews")
    public ResponseEntity<DataResponse<ReviewResponse>> createReview(@CurrentUser SysUserDetails ctx, @PathVariable Long productId, @RequestBody ReviewRequest reviewRequest) throws Exception {
        Review createdReview = reviewService.save(ctx.getId(), productId, reviewRequest);
        ReviewResponse reviewResponse = new ReviewResponse(createdReview);
        DataResponse<ReviewResponse> response = new DataResponse<ReviewResponse>(
                HttpStatus.CREATED.value(),
                "Review Created Succesfully",
                reviewResponse,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }
}
