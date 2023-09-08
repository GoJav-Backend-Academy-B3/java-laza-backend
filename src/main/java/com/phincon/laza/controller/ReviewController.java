package com.phincon.laza.controller;


import com.phincon.laza.model.dto.request.ReviewRequest;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.dto.response.ReviewResponse;
import com.phincon.laza.model.entity.Review;
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
    public ResponseEntity<DataResponse<List<ReviewResponse>>> getReviewsByProductId(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getReviewsByProductId(productId);
        List<ReviewResponse> reviewResponses = reviews.stream().map(ReviewResponse::new).collect(Collectors.toList());
        DataResponse<List<ReviewResponse>> dataResponse = new DataResponse<>(HttpStatus.OK.value(), "Success", reviewResponses, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @PostMapping("/{productId}/reviews")
    public ResponseEntity<DataResponse<Review>> createReview(@AuthenticationPrincipal UserDetails ctx, @PathVariable Long productId, @RequestBody ReviewRequest reviewRequest) throws Exception {
        Review createdReview = reviewService.save(ctx.getUsername(), productId, reviewRequest);

        DataResponse<Review> response = new DataResponse<>(
                HttpStatus.CREATED.value(),
                "Review Created Succesfully",
                createdReview,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }
}
