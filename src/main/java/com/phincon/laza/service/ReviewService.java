package com.phincon.laza.service;


import com.phincon.laza.model.dto.request.ReviewRequest;
import com.phincon.laza.model.entity.Category;
import com.phincon.laza.model.entity.Review;

import java.util.List;

public interface ReviewService {
    List<Review> getReviewsByProductId(Long productId);
   Review save(String fullName, Long productId, ReviewRequest reviewRequest) throws Exception;
}
