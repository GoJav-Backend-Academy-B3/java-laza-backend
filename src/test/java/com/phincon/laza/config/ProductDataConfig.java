package com.phincon.laza.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.phincon.laza.model.entity.Brand;
import com.phincon.laza.model.entity.Category;
import com.phincon.laza.model.entity.Product;
import com.phincon.laza.model.entity.Size;

@TestConfiguration
public class ProductDataConfig {

    public static List<Brand> brands = Arrays.asList(
            new Brand(1l, "FILA", "logoUrl", false, null),
            new Brand(2l, "NIKE", "logoUrl", false, null),
            new Brand(3l, "ADIDAS", "logoUrl", false, null));

    public static List<Size> sizes = Arrays.asList(
            new Size(1l, "S", null, false),
            new Size(2l, "M", null, false),
            new Size(3l, "L", null, false));

    public static List<Category> categories = Arrays.asList(
            new Category(1l, "Kemeja", null, false),
            new Category(2l, "Celana", null, false),
            new Category(3l, "Kaos", null, false));

    @Bean
    @Qualifier("product.all")
    public List<Product> productAll() {
        return Arrays.asList(
                new Product(1l, "product name 1", "product description 1", "image_url", 1, brands.get(0),
                        Arrays.asList(sizes.get(1), sizes.get(0)), categories.get(0)),
                new Product(2l, "Product name 2", "prduct description 2", "image_url", 2, brands.get(0),
                        Arrays.asList(sizes.get(0), sizes.get(1)), categories.get(1)),
                new Product(3l, "Product name 3", "produt descrition 3", "image_url", 3, brands.get(2),
                        Arrays.asList(sizes.get(1), sizes.get(2)), categories.get(0)),
                new Product(4l, "Product name 4", "prdocut cdesctionw 4", "image_urla", 4, brands.get(0),
                        Arrays.asList(sizes.get(0), sizes.get(1)), categories.get(1)),
                new Product(5l, "prodcutsd  naer 5", "peroidcut descoriwpton 5", "imgea-Url", 5, brands.get(0),
                        Arrays.asList(sizes.get(0)), categories.get(1)),
                new Product(6l, "prodcut namre 6", "descitpiton rp 6", "image_url", 6, brands.get(0),
                        Arrays.asList(sizes.get(1)), categories.get(2)));
    }

    @Bean
    @Qualifier("product.one")
    public Product productOne() {
        var p = new Product(99l, "New product 99", "Product 99 description", "image_url", 10, brands.get(2),
                Arrays.asList(sizes.get(0), sizes.get(1)), categories.get(0));
        p.setReviewList(Arrays.asList());
        return p;
    }

    @Bean
    @Qualifier("product.update")
    public Product productUpdated() {
        return new Product(99l, "Figure Kobo Kanaeru", "figure kobo kanaeru", "image+url", 1000000000,
                brands.get(0),
                Arrays.asList(sizes.get(0), sizes.get(2)), categories.get(1));
    }
}
