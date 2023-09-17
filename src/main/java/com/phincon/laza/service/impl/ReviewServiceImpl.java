package com.phincon.laza.service.impl;

    import com.phincon.laza.exception.custom.BadRequestException;
    import com.phincon.laza.exception.custom.ConflictException;
    import com.phincon.laza.model.dto.request.ReviewRequest;
    import com.phincon.laza.model.dto.response.ReviewsResponse;
    import com.phincon.laza.model.entity.Order;
    import com.phincon.laza.model.entity.Product;
    import com.phincon.laza.model.entity.Review;
    import com.phincon.laza.model.entity.User;
    import com.phincon.laza.repository.ReviewRepository;
    import com.phincon.laza.service.OrderService;
    import com.phincon.laza.service.ProductsService;
    import com.phincon.laza.service.ReviewService;
    import com.phincon.laza.service.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import java.time.LocalDateTime;
    import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Override
    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findReviewsByProductId(productId);
    }
    @Override
    public Review save(String userId,String orderId, Long productId, ReviewRequest reviewRequest) throws Exception {
        Order order = orderService.getOrderById(orderId);
        String orderUserId = order.getUser().getId();
        if (!userId.equals(orderUserId)) {
            throw new ConflictException("User is not authorized to add a review for this order.");
        }
        if (!"completed".equalsIgnoreCase(order.getOrderStatus())) {
            throw new ConflictException("Order is not completed, cannot add a review.");
        }
        Product product = productsService.getProductById(productId);
        User user = userService.getById(userId);
        Review review = new Review();
        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());
        review.setCreatedAt(LocalDateTime.now());
        review.setUser(user);
        review.setProduct(product);
        review.setOrder(order);
        reviewRepository.save(review);
        return review;
    }
    @Override
        public float calculateAverageRating(List<ReviewsResponse.ReviewItem> reviewItems) {
        if (reviewItems.isEmpty()) {
            return 0.0f;
        }

        double totalRating = reviewItems.stream()
                .mapToDouble(ReviewsResponse.ReviewItem::getRating)
                .sum();


        float averageRating = (float) (totalRating / reviewItems.size());
        return averageRating;
        }

}
