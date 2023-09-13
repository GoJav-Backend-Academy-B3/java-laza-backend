package com.phincon.laza.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.model.dto.request.SizeRequest;
import com.phincon.laza.model.entity.Size;
import com.phincon.laza.service.SizeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SizeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private SizeService sizeService;

    @InjectMocks
    private SizeController sizeController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Get All Sizes")
    public void testGetAllSizes() throws Exception {
        List<Size> sizes = Arrays.asList(
                new Size(1L, "Small"),
                new Size(2L, "Medium"),
                new Size(3L, "Large")
        );

        when(sizeService.getAllSize()).thenReturn(sizes);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/size"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        verify(sizeService, times(1)).getAllSize();
    }

//    @Test
//    @DisplayName("Get Size By Id")
//    public void testGetSizeById() throws Exception {
//        Long sizeId = 1L;
//        Size size = new Size(sizeId, "Small");
//
//        when(sizeService.getSizeById(sizeId)).thenReturn(size);
//
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/size/{id}", sizeId))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andReturn();
//
//        verify(sizeService, times(1)).getSizeById(sizeId);
//    }
//
//    @Test
//    @DisplayName("Create Size")
//    public void testCreateSize() throws Exception {
//        SizeRequest sizeRequest = new SizeRequest("New Size");
//        Size createdSize = new Size(4L, sizeRequest.getSize());
//
//        when(sizeService.save(any(SizeRequest.class))).thenReturn(createdSize);
//
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/size")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sizeRequest)))
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andReturn();
//
//        verify(sizeService, times(1)).save(any(SizeRequest.class));
//    }
//
//    @Test
//    @DisplayName("Update Size")
//    public void testUpdateSize() throws Exception {
//        Long sizeId = 1L;
//        SizeRequest sizeRequest = new SizeRequest("Updated Size");
//        Size updatedSize = new Size(sizeId, sizeRequest.getSize());
//
//        when(sizeService.update(eq(sizeId), any(SizeRequest.class))).thenReturn(updatedSize);
//
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/size/{id}", sizeId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sizeRequest)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andReturn();
//
//        verify(sizeService, times(1)).update(eq(sizeId), any(SizeRequest.class));
//    }
//
//    @Test
//    @DisplayName("Delete Size")
//    public void testDeleteSize() throws Exception {
//        Long sizeId = 1L;
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/size/{id}", sizeId))
//                .andExpect(status().isOk());
//
//        verify(sizeService, times(1)).delete(sizeId);
//    }
}

