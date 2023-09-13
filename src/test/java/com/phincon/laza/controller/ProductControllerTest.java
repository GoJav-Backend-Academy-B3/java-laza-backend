package com.phincon.laza.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.phincon.laza.config.ProductDataConfig;
import com.phincon.laza.exception.CustomExceptionHandler;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.CreateUpdateProductRequest;
import com.phincon.laza.model.entity.Product;
import com.phincon.laza.service.ProductsService;

@SpringJUnitConfig({ ProductDataConfig.class })
@TestInstance(Lifecycle.PER_CLASS)
public class ProductControllerTest {
    ProductsService service;
    ProductsController controller;
    MockMvc mockmvc;
    @Autowired
    @Qualifier("product.all")
    private List<Product> products;
    @Autowired
    @Qualifier("product.one")
    private Product productOne;
    @Autowired
    @Qualifier("product.update")
    private Product productUpdated;

    @BeforeAll
    public void setup() {
        service = Mockito.mock(ProductsService.class);
        controller = new ProductsController(service);
        mockmvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(CustomExceptionHandler.class).build();
    }

    @Test
    @DisplayName("Verify injected controller is not null")
    public void controllerInject_ok() {
        Assertions.assertNotNull(controller);
    }

    @Test
    @DisplayName("Get all product listed without any parameter should return ok")
    public void getAllProductNoParam_ok() throws Exception {
        assert (products != null);
        var page = new PageImpl<>(products, PageRequest.of(0, 10), products.size());
        Mockito.when(service.getAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(page);
        var action = mockmvc.perform(MockMvcRequestBuilders.get("/product"));
        action.andExpectAll(MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                MockMvcResultMatchers.jsonPath("$.metadata.page", Matchers.equalTo(0)),
                MockMvcResultMatchers.jsonPath("$.metadata.count", Matchers.equalTo(products.size())),
                MockMvcResultMatchers.jsonPath("$.data").isArray(),
                MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(products.size())));
        Mockito.verify(service, Mockito.times(1)).getAll(0, 10);
    }

    @Test
    @DisplayName("Get one product should ok and return data")
    public void getOneProductId_ok() throws Exception {
        assert (productOne != null);
        var product = productOne;
        Long requestId = product.getId();
        Mockito.when(service.getProductById(Mockito.anyLong())).thenReturn(product);
        var action = mockmvc.perform(MockMvcRequestBuilders.get("/product/{id}", requestId));
        action.andExpectAll(MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                MockMvcResultMatchers.jsonPath("$.data.id").value(requestId),
                MockMvcResultMatchers.jsonPath("$.data.name").value(product.getName()),
                MockMvcResultMatchers.jsonPath("$.data.description").value(product.getDescription()),
                MockMvcResultMatchers.jsonPath("$.data.category").exists(),
                MockMvcResultMatchers.jsonPath("$.data.brand").exists(),
                MockMvcResultMatchers.jsonPath("$.data.review").exists(),
                MockMvcResultMatchers.jsonPath("$.data.sizes").exists());
        Mockito.verify(service, Mockito.times(1)).getProductById(requestId);
    }

    @Test
    @DisplayName("Add one product should OK and return data")
    public void addOneProduct_ok() throws Exception {
        assert (productOne != null);
        var product = productOne;
        var requestData = new CreateUpdateProductRequest(product.getName(), product.getDescription(),
                product.getPrice(), null, product.getSizes().stream().map(v -> v.getId()).collect(Collectors.toList()),
                product.getCategory().getId(), product.getBrand().getId());
        Mockito.when(service.create(Mockito.any(CreateUpdateProductRequest.class))).thenReturn(product);
        var request = MockMvcRequestBuilders.multipart(HttpMethod.POST, "/product").param("name", requestData.name())
                .param("description", requestData.description()).param("price", requestData.price().toString())
                .param("categoryId", requestData.categoryId().toString())
                .param("brandId", requestData.brandId().toString());
        requestData.sizeIds().forEach(v -> request.param("sizeIds", v.toString()));
        var action = mockmvc.perform(request);
        action.andExpectAll(MockMvcResultMatchers.status().isCreated(),
                MockMvcResultMatchers.jsonPath("$.data.name").value(requestData.name()),
                MockMvcResultMatchers.jsonPath("$.data.description").value(requestData.description()),
                MockMvcResultMatchers.jsonPath("$.data.price").value(requestData.price()),
                MockMvcResultMatchers.jsonPath("$.data.category.id").value(requestData.categoryId()),
                MockMvcResultMatchers.jsonPath("$.data.category.category").value(product.getCategory().getCategory()),
                MockMvcResultMatchers.jsonPath("$.data.brand.id").value(requestData.brandId()),
                MockMvcResultMatchers.jsonPath("$.data.brand.name").value(product.getBrand().getName()),
                MockMvcResultMatchers.jsonPath("$.data.sizes").isArray(),
                MockMvcResultMatchers.jsonPath("$.data.sizes[*].size").exists());
        Mockito.verify(service, Mockito.times(1)).create(requestData);
    }

    @Test
    @DisplayName("Add one product with one of nonexisting id (brand, size, category) should NotFound")
    public void addOneProductIdNonexistent_404() throws Exception {
        assert (productOne != null);
        var product = productOne;
        var requestData = new CreateUpdateProductRequest(product.getName(), product.getDescription(),
                product.getPrice(), null, product.getSizes().stream().map(v -> v.getId()).collect(Collectors.toList()),
                product.getCategory().getId(), 120l);
        Mockito.when(service.create(Mockito.any(CreateUpdateProductRequest.class))).thenThrow(NotFoundException.class);
        var request = MockMvcRequestBuilders.multipart("/product").param("name", requestData.name())
                .param("description", requestData.description()).param("price", requestData.price().toString())
                .param("categoryId", requestData.categoryId().toString())
                .param("brandId", requestData.brandId().toString());
        requestData.sizeIds().forEach(v -> request.param("sizeIds", v.toString()));
        var action = mockmvc.perform(request);
        action.andExpectAll(MockMvcResultMatchers.status().isNotFound());
    }
}
