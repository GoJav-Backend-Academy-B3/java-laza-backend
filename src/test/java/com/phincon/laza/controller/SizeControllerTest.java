package com.phincon.laza.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.exception.custom.NotFoundException;

import com.phincon.laza.model.dto.request.AddressRequest;
import com.phincon.laza.model.dto.request.SizeRequest;

import com.phincon.laza.model.entity.Size;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.SizeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SizeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SizeService sizeService;

    @Autowired
    private SizeController sizeController;

    private SysUserDetails userDetail;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void init() throws Exception {
        SizeRequest request = new SizeRequest();
        request.setSize("Small");

        Size size = new Size();
        size.setId(1L);

        Size size1 = new Size();
        size1.setId(2L);

        List<Size> sizes = new ArrayList<>();
        sizes.add(size);
        sizes.add(size1);

        lenient().when(sizeService.save(request)).thenReturn(size);
        lenient().when(sizeService.getAllSize()).thenReturn(sizes);
        lenient().when(sizeService.getSizeById(1L)).thenReturn(size);
        lenient().when(sizeService.update(1L, request)).thenReturn(size);
        lenient().doNothing().when(sizeService).delete(1L);

        lenient().when(sizeService.getSizeById(2L)).thenThrow(new NotFoundException("Size not found"));
        lenient().doThrow(new NotFoundException("Size not found")).when(sizeService).delete(2L);

        userDetail = new SysUserDetails("1", "mawitra", "password",
                Arrays.asList(new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("ADMIN")));
    }

    @Test
    @DisplayName("Add Size Success")
    public void whenAddRequestToSize_thenCorrectResponse() throws Exception {
        SizeRequest request = new SizeRequest();
        request.setSize("Small");

        mockMvc.perform(MockMvcRequestBuilders.post("/size").with(user(userDetail))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status_code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("Size created successfully"))
                .andExpect(jsonPath("$.data.id").value(1));

        // Verifikasi bahwa metode sizeService.save dipanggil dengan argumen yang benar
        verify(sizeService, times(1)).save(request);
    }
    @Test
    @DisplayName("Add Size when request is required")
    public void whenAddRequestToAddressAndRequestIsRequired_thenFailedResponse() throws Exception {
        SizeRequest request = new SizeRequest();

        Size size = new Size();
        size.setId(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/size").with(user(userDetail))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.Size").value("Size is required"));
    }


    @Test
    @DisplayName("Get all Sizes Success")
    public void whenGetAllSizes_thenCorrectResponse() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/size").with(user(userDetail)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isArray());

        verify(sizeService, times(1)).getAllSize();
    }

    @Test
    @DisplayName("Get Size by ID Success")
    public void whenGetSizeById_thenCorrectResponse() throws Exception {
        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/size/{id}", id).with(user(userDetail)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").exists());

        // Verifikasi bahwa metode sizeService.getSizeById dipanggil dengan argumen yang benar
        verify(sizeService, times(1)).getSizeById(1L);
    }

    @Test
    @DisplayName("Get Size by ID not found")
    public void whenGetSizeByIdNotFound_thenCorrectResponse() throws Exception {
        Long id = 2L;

        mockMvc.perform(MockMvcRequestBuilders.get("/size/{id}", id).with(user(userDetail)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Size not found"));

        verify(sizeService, times(1)).getSizeById(2L);
    }

    @Test
    @DisplayName("Update Size Success")
    public void whenUpdateSize_thenCorrectResponse() throws Exception {
        SizeRequest request = new SizeRequest();
        Long id = 1L;
        request.setSize("Small");

        mockMvc.perform(MockMvcRequestBuilders.put("/size/{id}/update", id).with(user(userDetail))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
//                .andExpect(jsonPath("$.message").value("Ukuran berhasil diperbarui"))
                .andExpect(jsonPath("$.data.id").exists());

        verify(sizeService, times(1)).update(id, request);
    }

    @Test
    @DisplayName("Delete Size Success")
    public void whenDeleteSize_thenCorrectResponse() throws Exception {
        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/size/{id}", id).with(user(userDetail)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Size deleted successfully"));

        verify(sizeService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("Delete Size failed")
    public void whenDeleteSizeAndIdNotFound_thenFailedResponse() throws Exception {
        Long id = 2L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/size/{id}", id).with(user(userDetail)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Size not found"));

        verify(sizeService, times(1)).delete(2L);
    }
}
