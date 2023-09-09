package com.phincon.laza.model.dto.response;


import com.phincon.laza.model.entity.Product;
import lombok.Data;

@Data
public class WishlistResponse {
    private Long productId;
    private String productName;
    private String productImage_url;
    private Integer price;
    private String brandName;

    public WishlistResponse(Product product){
        this.productId = product.getId();
        this.productName = product.getName();
        this.productImage_url = product.getImageUrl();
        this.price = product.getPrice();
        this.brandName = product.getBrand().getName();

    }
}
