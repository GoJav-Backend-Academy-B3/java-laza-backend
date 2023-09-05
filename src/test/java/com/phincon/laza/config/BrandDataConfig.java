package com.phincon.laza.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.phincon.laza.model.entity.Brand;

@TestConfiguration
public class BrandDataConfig {
  @Bean("brand.all")
  public List<Brand> brandAll() {
    return Arrays.asList(
        new Brand(1l, "FILA", "logoUrl", null),
        new Brand(2l, "NIKE", "logoUrl", null),
        new Brand(3l, "ADIDAS", "logoUrl", null),
        new Brand(4l, "H&M", "logoUrl", null),
        new Brand(5l, "Zara", "logoUrl", null),
        new Brand(6l, "Uniqlo", "logoUrl", null));
  }

  @Bean("brand.one")
  public Brand brandOne() {
    return new Brand(null, "3Second", "logoUrl", null);
  }

  @Bean("brand.one.dup")
  public Brand brandOneDup() {
    return brandAll().get(5);
  }
}
