package com.phincon.laza.config;


import com.phincon.laza.model.entity.Brand;
import com.phincon.laza.model.entity.Category;
import com.phincon.laza.model.entity.Product;
import com.phincon.laza.model.entity.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@TestConfiguration
public class WishlistDataConfig {

    @Bean
    @Qualifier("category.all")
    public List<Category> categoryAll(){
        return Arrays.asList(new Category(0l,"categoryA",null,false)
        );
    }
    @Bean
    @Qualifier("brand.all")
    public List<Brand> brandAll(){
        return Arrays.asList(
                new Brand(0l,"BrandA","logo",false, null)
        );
    }
    @Bean
    @Qualifier("product.all")
    public List<Product> productAll(){
        return Arrays.asList(
                new Product(90l, "product1", "desc1","image",10000, LocalDateTime.now(),"test", new Brand(10l,"BrandA","logo",false, null),null,new Category(1l,"categoryA",null,false),null,null,null),
                new Product(91l, "product2", "desc2","image",10000, LocalDateTime.now(),"test", new Brand(10l,"BrandA","logo",false, null),null,new Category(1l,"categoryA",null,false),null,null,null),
                new Product(93l, "product3", "desc1","image",10000, LocalDateTime.now(),"test",new Brand(10l,"BrandB","logo",false, null),null,new Category(1l,"categoryA",null,false),null,null,null),
                new Product(94l, "product4", "desc2","image",10000, LocalDateTime.now(),"test",new Brand(10l,"BrandB","logo",false, null),null,new Category(1l,"categoryA",null,false),null,null,null)
        );
    }


    @Bean
    @Qualifier("user.all")
    public List<User> userAll(){
        List<Product> products = Arrays.asList(
                new Product(90l, "product1", "desc1","image",10000, LocalDateTime.now(),"test",null,null,null,null,null,null),
                new Product(91l, "product2", "desc2","image",10000, LocalDateTime.now(),"test",null,null,null,null,null,null)
        );
        List<Product> products2= Arrays.asList(
                new Product(93l, "product3", "desc1","image",10000, LocalDateTime.now(),"test",null,null,null,null,null,null),
                new Product(94l, "product4", "desc2","image",10000, LocalDateTime.now(),"test",null,null,null,null,null,null)
        );

        return Arrays.asList(
                new User("23", "user1", "user1", "password", "email", "image",true, null,null,products,null,null,null,null,null,null),
                new User("24", "user2", "user2", "password", "email", "image",true, null,null,products2,null,null,null,null,null,null)
        );
    }
}
