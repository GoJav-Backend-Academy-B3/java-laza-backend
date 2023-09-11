package com.phincon.laza.service.impl;

    import com.phincon.laza.model.dto.request.ReviewRequest;
    import com.phincon.laza.model.dto.response.ReviewResponse;
    import com.phincon.laza.model.dto.response.ReviewsResponse;
    import com.phincon.laza.model.entity.Product;
    import com.phincon.laza.model.entity.Review;
    import com.phincon.laza.model.entity.User;
    import com.phincon.laza.repository.ReviewRepository;
    import com.phincon.laza.service.ProductsService;
    import com.phincon.laza.service.ReviewService;
    import com.phincon.laza.service.UserService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;
    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductsService productsService;
    private final UserService userService;

    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findReviewsByProductId(productId);
    }

    public Review save(String id,Long productId, ReviewRequest reviewRequest) throws Exception {
        Product product = productsService.getProductById(productId);
        User user = userService.getById(id);
        Review review = new Review();
        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());
        review.setCreatedAt(LocalDateTime.now());
        review.setUser(user);
        review.setProduct(product);
        reviewRepository.save(review);
        return review;
    }

    public float calculateAverageRating(List<ReviewsResponse.ReviewItem> reviewItems) {
        if (reviewItems.isEmpty()) {
            return 0.0f;
        }

        float totalRating = 0.0f;
        for (ReviewsResponse.ReviewItem reviewItem : reviewItems) {
            totalRating += reviewItem.getRating();
        }

        return totalRating / reviewItems.size();
    }

}
