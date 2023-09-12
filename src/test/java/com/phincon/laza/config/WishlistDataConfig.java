package com.phincon.laza.config;


import com.phincon.laza.model.entity.Brand;
import com.phincon.laza.model.entity.Category;
import com.phincon.laza.model.entity.Product;
import com.phincon.laza.model.entity.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TestConfiguration
public class WishlistDataConfig {

    @Bean
    @Qualifier("category.all")
    public List<Category> categoryAll(){
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(0l,"categoryA",null,false));
        return categories;
    }
    @Bean
    @Qualifier("brand.all")
    public List<Brand> brandAll(){
        List<Brand> brands = new ArrayList<>();
        brands.add(new Brand(0l,"BrandA","logo",false, null));
        return brands;
    }
    @Bean
    @Qualifier("product.all")
    public List<Product> productAll(){
        List<User> user = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        user.add(new User("23", "user1", "user1", "password", "email", "image",true, null,null, null,null,null,null,null,null,null));
        products.add(new Product(90l, "product1", "desc1","image",10000, LocalDateTime.now(),"test", new Brand(10l,"BrandA","logo",false, null),null,new Category(1l,"categoryA",null,false),user,null,null));
        products.add(new Product(91l, "product2", "desc2","image",10000, LocalDateTime.now(),"test", new Brand(10l,"BrandA","logo",false, null),null,new Category(1l,"categoryA",null,false),user,null,null));
        return products;
    }


    @Bean
    @Qualifier("user.all")
    public List<User> userAll(){
        List<User> newUser= new ArrayList<>();
        List<Product> products = new ArrayList<>();

        newUser.add(new User("23", "user1", "user1", "password", "email", "image",true, null,null, null ,null,null,null,null,null,null));
        products.add(new Product(90l, "product1", "desc1","image",10000, LocalDateTime.now(),"test",null,null,null,newUser,null,null));
        products.add(new Product(91l, "product2", "desc2","image",10000, LocalDateTime.now(),"test",null,null,null,newUser,null,null));


        return new ArrayList<>(Arrays.asList(
                new User("23", "user1", "user1", "password", "email", "image",true, null,null,products,null,null,null,null,null,null)
        ));
    }

    @Bean
    @Qualifier("user.one")
    public User userOne(){
        List<Product> products = new ArrayList<>(Arrays.asList(
                new Product(90l, "product1", "desc1","image",10000, LocalDateTime.now(),"test",null,null,null,null,null,null),
                new Product(91l, "product2", "desc2","image",10000, LocalDateTime.now(),"test",null,null,null,null,null,null)
        ));
        return new User("23", "user1", "user1", "password", "email", "image",true, null,null,products,null,null,null,null,null,null);
    }
}
