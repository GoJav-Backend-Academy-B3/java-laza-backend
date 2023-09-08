package com.phincon.laza.service.impl;

import com.phincon.laza.exception.custom.NotFoundException;
        import com.phincon.laza.model.dto.request.ReviewRequest;
        import com.phincon.laza.model.entity.Product;
        import com.phincon.laza.model.entity.Review;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.repository.ProductsRepository;
        import com.phincon.laza.repository.ReviewRepository;
import com.phincon.laza.service.ProductsService;
import com.phincon.laza.service.ReviewService;
import com.phincon.laza.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;
        import java.time.LocalDateTime;
        import java.util.List;

@Service
@RequiredArgsConstructor

public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductsService productsService;
    private final UserService userService;


   @Override

    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findReviewsByProductId(productId);
    }

    @Override
    public Review save(String fullName,Long productId, ReviewRequest reviewRequest) throws Exception {

        Product product = productsService.getProductById(productId);

        User user = userService.getByUsername(fullName);

        Review review = new Review();
        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());
        review.setCreatedAt(LocalDateTime.now());
        review.setUser(user);
        review.setProduct(product);

        return reviewRepository.save(review);
    }
}
