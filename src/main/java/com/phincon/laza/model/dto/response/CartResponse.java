package com.phincon.laza.model.dto.response;

import com.phincon.laza.model.entity.Cart;
import lombok.Data;

@Data
public class CartResponse {
    private Long cartId;
    private Long productId;
    private String productName;
    private String productImage_url;
    private Integer price;
    private Long sizeId;
    private String sizeName;
    private Integer quantity;


    public CartResponse(Cart cart){
        this.cartId = cart.getId();
        this.productId = cart.getProduct().getId();
        this.productName = cart.getProduct().getName();
        this.productImage_url = cart.getProduct().getImageUrl();
        this.price = cart.getProduct().getPrice();
        this.sizeId = cart.getSize().getId();
        this.sizeName = cart.getSize().getSize();
        this.quantity = cart.getQuantity();
    }
}
