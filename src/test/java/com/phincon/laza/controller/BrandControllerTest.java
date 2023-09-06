package com.phincon.laza.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;

import java.io.InputStream;
import java.util.List;

import javax.print.attribute.standard.Media;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.config.BrandDataConfig;
import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.Brand;

@WebMvcTest(controllers = BrandController.class)
@Import({ BrandDataConfig.class })
public class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BrandService service;

    @Autowired
    private BrandController controller;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    @Qualifier("brand.all")
    private List<Brand> brandAll;

    @Autowired
    @Qualifier("brand.one")
    private Brand brandOne;

    @Autowired
    @Qualifier("brand.one.dup")
    private Brand brandOneDup;

    @Test
    public void whenDokterControllerInjected_thenNotNull() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    @DisplayName("Get request to brand with page 1 size 4 should return OK with data")
    public void getBrandPage1Size4_data() {
        Page<Brand> brandPage = new PageImpl<>(brandAll, PageRequest.of(1, 4), brandAll.size());
        Mockito.when(service.findAll(1, 4).thenReturn(brandPage);

        // act
        mockMvc.perform(MockMvcRequestBuilders.get("/api/brands"))
               .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.metadata.page").value(1),
                        MockMvcResultMatchers.jsonPath("$.metatada.size").value(4),
                        MockMvcResultMatchers.jsonPath("$.metadata.count").value(2),
                        MockMvcResultMatchers.jsonPath("$.data[*].name").exists(),
                        MockMvcResultMatchers.jsonPath("$.data[*].logo_url").exists());
    }

    @Test
    @DisplayName("Get one request to brand should return OK with data")
    public void getOneBrand_data() {
        long desiredId = 10l;
        Brand data = brandOne;
        Mockito.when(service.findById(anyLong())).thenReturn(data);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/brands/{id}", desiredId))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.status_code").value(200),
                        MockMvcResultMatchers.jsonPath("$.data.id").value(data.getId()),
                        MockMvcResultMatchers.jsonPath("$.data.logo_url").value(data.getLogoUrl()),
                        MockMvcResultMatchers.jsonPath("$.data.name").value(data.getName()));

    }

    @Test
    @DisplayName("Get one request to brand should return NOT_FOUND")
    public void getOneBrand_notFound() {
        long desiredId = 10l;
        Brand data = brandOne;
        Mockito.when(service.findById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/brands/{id}", desiredId))
                .andExpectAll(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Add one request to brand should return data")
    public void postOneBrand_data() {
        var data = brandOne;
        var mockMultipart = new MockMultipartFile("brand1.jpg", InputStream.nullInputStream());
        Mockito.when(service.add(brandOne.getName(), mockMultipart)).thenReturn(data);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .multipart("/api/brands")
                        .file(mockMultipart)
                        .param("name", data.getName()))
                .andExpectAll(MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.data.name").value(data.getName()));
    }

    @Test
    @DisplayName("POST one request to brand should conflict if brand name exists")
    public void postOneBrand_conflict() {
        var data = brandOne;
        var mockMultipart = new MockMultipartFile("brand1.jpg", InputStream.nullInputStream());
        Mockito.when(service.add(brandOne.getName(), mockMultipart)).thenThrow(ConflictException.class);

        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/api/brands")
                        .file(mockMultipart)
                        .param("name", data.getName()))
                .andExpectAll(MockMvcResultMatchers.status().isConflict(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("PUT request to brand should return updated data")
    public void putBrand_data() {
        long desiredId = 10l;
        var data = brandOne;
        var mockMultipart = new MockMultipartFile("brand1.jpg", InputStream.nullInputStream());
        Mockito.when(service.update(desiredId, data.getName(), mockMultipart)).thenReturn(data);

        mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/api/brands/{id}", desiredId)
                        .file(mockMultipart)
                        .param("name", data.getName()))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.jsonPath("$.data.name").value(data.getName()));
    }

    @Test
    @DisplayName("PUT request to brand should return conflict")
    public void putBrand_conflict() {
        long desiredId = 10l;
        var data = brandOne;
        var mockMultipart = new MockMultipartFile("brand1.jpg", InputStream.nullInputStream());
        Mockito.when(service.update(desiredId, data.getName(), mockMultipart)).thenThrow(ConflictException.class);

        mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/api/brands/{id}", desiredId)
                        .file(mockMultipart)
                        .param("name", data.getName()))
                .andExpectAll(MockMvcResultMatchers.status().isConflict(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("DELETE request to brand should ok")
    public void deleteBrand_ok() {
        long desiredId = 10l;
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/brands/{id}", desiredId))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
        verify(service, times(1)).delete(desiredId);
    }

    @Test
    @DisplayName("DELETE request to brand should not found")
    public void deleteBrand_notFound() {
        long desiredId = 10l;
        doThrow(NotFoundException.class).when(service.delete(desiredId));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/brands/{id}", desiredId))
            .andExpectAll(MockMvcResultMatchers.status().isNotFound(),
            MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        verify(service, times(1)).delete(desiredId);
    }
}
