package com.phincon.laza.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.model.entity.PaymentMethod;
import com.phincon.laza.security.jwt.JwtAuthenticationFilter;
import com.phincon.laza.service.PaymentMethodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentMethodController.class)
public class PaymentMethodControllerTest {

    @MockBean
    private PaymentMethodService paymentMethodService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private PaymentMethodController paymentMethodController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        // Mock data
        PaymentMethod paymentMethod1 = new PaymentMethod();
        paymentMethod1.setId(1L);
        paymentMethod1.setName("Sample Payment Method");
        paymentMethod1.setCode("SAMPLE_CODE");
        paymentMethod1.setType("Sample Type");
        paymentMethod1.setProvider("Sample Provider");
        paymentMethod1.setAdminFee(10);
        paymentMethod1.setIsActive(true);
        paymentMethod1.setLogoUrl("https://example.com/logo.png");

        PaymentMethod paymentMethod2 = new PaymentMethod();
        paymentMethod2.setId(2L);
        paymentMethod2.setName("Sample Payment Method 2");
        paymentMethod2.setCode("SAMPLE_CODE 2");
        paymentMethod2.setType("Sample Type 2");
        paymentMethod2.setProvider("Sample Provider 2");
        paymentMethod2.setAdminFee(20);
        paymentMethod2.setIsActive(false);
        paymentMethod2.setLogoUrl("https://example.com/logo2.png");


        List<PaymentMethod> paymentMethods = Arrays.asList(paymentMethod1, paymentMethod2);

        // Mock Service
        lenient().when(paymentMethodService.getAllPaymentMethods()).thenReturn(paymentMethods);
        lenient().when(paymentMethodService.getPaymentMethodById(1L)).thenReturn(paymentMethod1);
    }

    @Test
    public void testControllerInjected_thenNotNull() {
        assertThat(paymentMethodController).isNotNull();
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAllPaymentMethods() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/payment-methods")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(200))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].name").value("Sample Payment Method"))
                .andExpect(jsonPath("$.data[0].code").value("SAMPLE_CODE"))
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].name").value("Sample Payment Method 2"))
                .andExpect(jsonPath("$.data[1].code").value("SAMPLE_CODE 2"))
                .andReturn();

        verify(paymentMethodService, times(1)).getAllPaymentMethods();
    }

    @Test
    @WithMockUser(authorities = "USER")
    void testGetPaymentMethodById() throws Exception {


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/payment-methods/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].name").value("Sample Payment Method"))
                .andExpect(jsonPath("$.data[0].code").value("SAMPLE_CODE"))
                .andReturn();

        PaymentMethod response = objectMapper.readValue(result.getResponse().getContentAsString(), PaymentMethod.class);
        // Add assertions for the response data as needed
    }

    @Test
    void testCreatePaymentMethod() throws Exception {
        PaymentMethod paymentMethod = new PaymentMethod();
        when(paymentMethodService.createPaymentMethod(any())).thenReturn(paymentMethod);

        String jsonBody = objectMapper.writeValueAsString(paymentMethod);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/management/payment-methods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated())
                .andReturn();

        PaymentMethod response = objectMapper.readValue(result.getResponse().getContentAsString(), PaymentMethod.class);
        // Add assertions for the response data as needed
    }

    @Test
    void testUpdatePaymentMethod() throws Exception {
        Long id = 1L;
        PaymentMethod updatedPaymentMethod = new PaymentMethod();
        when(paymentMethodService.updatePaymentMethod(eq(id), any())).thenReturn(updatedPaymentMethod);

        String jsonBody = objectMapper.writeValueAsString(updatedPaymentMethod);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/management/payment-methods/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andReturn();

        PaymentMethod response = objectMapper.readValue(result.getResponse().getContentAsString(), PaymentMethod.class);
        // Add assertions for the response data as needed
    }

    // Add more test methods for other controller endpoints

    // Ensure to test different scenarios, including error cases
}

