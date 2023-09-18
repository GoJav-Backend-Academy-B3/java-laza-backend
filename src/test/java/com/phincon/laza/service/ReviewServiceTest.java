package com.phincon.laza.service;

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
import com.phincon.laza.service.UserService;
import com.phincon.laza.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductsService productsService;

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("Get Reviews by Product ID")
    void getReviewsByProductId() {
        Long productId = 1L;
        List<Review> expectedReviews = new ArrayList<>();
        // Anda dapat menambahkan objek Review ke dalam expectedReviews sesuai kebutuhan

        when(reviewRepository.findReviewsByProductId(productId)).thenReturn(expectedReviews);

        List<Review> result = reviewService.getReviewsByProductId(productId);

        // Memastikan bahwa hasil yang diterima sesuai dengan yang diharapkan
        assertEquals(expectedReviews, result);

        // Verifikasi bahwa metode reviewRepository.findReviewsByProductId dipanggil dengan benar
        verify(reviewRepository, times(1)).findReviewsByProductId(productId);
    }
    @Test
    @DisplayName("Save Review Successfully")
    void saveReviewSuccessfully() throws Exception {
        String userId = "user123";
        String orderId = "order123";
        Long productId = 1L;

        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus("completed");

        User user = new User();
        user.setId(userId);
        order.setUser(user);

        Product product = new Product();
        product.setId(productId);

        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setComment("Great product!");
        reviewRequest.setRating(5);

        when(orderService.getOrderById(orderId)).thenReturn(order);
        when(productsService.getProductById(productId)).thenReturn(product);
        when(userService.getById(userId)).thenReturn(user);

        Review savedReview = new Review();
        savedReview.setId(1L);
        savedReview.setComment(reviewRequest.getComment());
        savedReview.setRating(reviewRequest.getRating());
        savedReview.setCreatedAt(LocalDateTime.now());
        savedReview.setUser(user);
        savedReview.setProduct(product);
        savedReview.setOrder(order);

        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);

        Review result = reviewService.save(userId, orderId, productId, reviewRequest);

        assertNotNull(result);
        assertEquals(userId, result.getUser().getId());
        assertEquals(orderId, result.getOrder().getId());
        assertEquals(productId, result.getProduct().getId());
        assertEquals(reviewRequest.getComment(), result.getComment());
        assertEquals(reviewRequest.getRating(), result.getRating());
        assertNotNull(result.getCreatedAt());

        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    @DisplayName("Save Review Unauthorized User")
    void saveReviewUnauthorizedUser() throws Exception {
        String userId = "user123";
        String orderId = "order123";
        Long productId = 1L;

        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus("completed");

        User user = new User();
        user.setId("anotherUser123");
        order.setUser(user);

        when(orderService.getOrderById(orderId)).thenReturn(order);
        when(userService.getById(userId)).thenReturn(user);

        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setComment("Great product!");
        reviewRequest.setRating(5);

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            reviewService.save(userId, orderId, productId, reviewRequest);
        });

        assertEquals("User is not authorized to add a review for this order.", exception.getMessage());

        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    @DisplayName("Save Review Order Not Completed")
    void saveReviewOrderNotCompleted() throws Exception {
        String userId = "user123";
        String orderId = "order123";
        Long productId = 1L;

        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus("pending");

        User user = new User();
        user.setId("user123");
        order.setUser(user);

        when(orderService.getOrderById(orderId)).thenReturn(order);

        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setComment("Great product!");
        reviewRequest.setRating(5);

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            reviewService.save(userId, orderId, productId, reviewRequest);
        });

        assertEquals("Order is not completed, cannot add a review.", exception.getMessage());

        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    @DisplayName("Calculate Average Rating - Empty List")
    void calculateAverageRatingEmptyList() {
        List<ReviewsResponse.ReviewItem> reviewItems = new ArrayList<>();

        float averageRating = reviewService.calculateAverageRating(reviewItems);

        assertEquals(0.0f, averageRating, 0.001f);
    }
}


