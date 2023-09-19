package com.phincon.laza.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllWishlist {
    private Integer getTotalPages;
    private Long getTotalElements;
    private List<WishlistResponse> products;
}
