package com.phincon.laza.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.phincon.laza.model.entity.Size;

import java.util.Arrays;
import java.util.List;

@TestConfiguration
public class SizeDataConfig {

    public static List<Size> sizeInit() {
        return Arrays.asList(
                new Size(1L, "S", null, false),
                new Size(2L, "M", null, false),
                new Size(3L, "L", null, false));
    }

    @Bean
    @Qualifier("size.all")
    public List<Size> sizeAll() {
        return sizeInit();
    }

    @Bean
    @Qualifier("size.one")
    public Size sizeOne() {
        return new Size(99L, "New Size 99", null, false);
    }

    @Bean
    @Qualifier("size.update")
    public Size sizeUpdated() {
        return new Size(99L, "Updated Size 99", null, false);
    }
}
