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

    public static List<Brand> brandInit() {
        return Arrays.asList(
                new Brand(1l, "FILA", "logoUrl", null),
                new Brand(2l, "NIKE", "logoUrl", null),
                new Brand(3l, "ADIDAS", "logoUrl", null));
    }

    public static List<Size> sizeInit() {
        return Arrays.asList(
                new Size(1l, "S", null),
                new Size(2l, "M", null),
                new Size(3l, "L", null));
    }

    public static List<Category> categoryInit() {
        return Arrays.asList(
                new Category(1l, "Kemeja"),
                new Category(2l, "Celana"),
                new Category(3l, "Kaos"));
    }

    @Bean
    @Qualifier("product.all")
    public List<Product> productAll() {
        return Arrays.asList(
                new Product(1l, "product name 1", "product description 1", "image_url", 1, null,
                        Arrays.asList(sizeInit().get(1), sizeInit().get(0)), brandInit().get(0), categoryInit().get(0),
                        null, null),
                new Product(2l, "Product name 2", "prduct description 2", "image_url", 2, null,
                        Arrays.asList(sizeInit().get(0), sizeInit().get(1)), brandInit().get(1), categoryInit().get(1),
                        null, null),
                new Product(3l, "Product name 3", "produt descrition 3", "image_url", 3, null,
                        Arrays.asList(sizeInit().get(1), sizeInit().get(2)), brandInit().get(2), categoryInit().get(0),
                        null, null),
                new Product(4l, "Product name 4", "prdocut cdesctionw 4", "image_urla", 4, null,
                        Arrays.asList(sizeInit().get(0), sizeInit().get(1)), brandInit().get(0), categoryInit().get(1),
                        null, null),
                new Product(5l, "prodcutsd  naer 5", "peroidcut descoriwpton 5", "imgea-Url", 5, null,
                        Arrays.asList(sizeInit().get(0)), brandInit().get(0), categoryInit().get(1),
                        null, null),
                new Product(6l, "prodcut namre 6", "descitpiton rp 6", "image_url", 6, null,
                        Arrays.asList(sizeInit().get(1)), brandInit().get(0), categoryInit().get(2),
                        null, null));
    }

    @Bean
    @Qualifier("product.one")
    public Product productOne() {
        return new Product(99l, "New product 99", "Product 99 description", "image_url", 10, null,
                Arrays.asList(sizeInit().get(0), sizeInit().get(1)), brandInit().get(2), categoryInit().get(0),
                null, null);
    }

    @Bean
    @Qualifier("product.sz.nx")
    public Product productSizeNonexistent() {
        return new Product(null, "Figure Kobo Kanaeru", "figure kobo kanaeru", "image+url", 1000000000, null,
        Arrays.asList(new Size(90l, null, null)), brandInit().get(0), categoryInit().get(1), null, null);
    }
}
