package com.phincon.laza.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.phincon.laza.model.entity.CreditCard;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.CreditCardService;

@SpringBootTest
@AutoConfigureMockMvc
public class CreditCardControllerTest {

    @MockBean
    private CreditCardService service;

    @Autowired 
    private CreditCardController controller;

    @Autowired
    private MockMvc mockmvc;

    @Autowired
    @Qualifier("cc.all")
    private List<CreditCard> creditCards;

    @Test
    @DisplayName("Get all credit card that user have should returnd data")
    public void getAllCCUser_data() throws Exception {
        final String userId = "user1";
        Mockito.when(service.getAll(Mockito.eq(userId))).thenReturn(creditCards);
        SysUserDetails ud = new SysUserDetails(userId, "asdf", "ghjkl",
                Arrays.asList(new SimpleGrantedAuthority("USER")));

        var action = mockmvc.perform(MockMvcRequestBuilders.get("/cc").with(user(ud)));

        action.andExpectAll(MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                MockMvcResultMatchers.jsonPath("$.data").isArray(),
                MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(creditCards.size())));

        Mockito.verify(service, Mockito.times(1)).getAll(Mockito.eq(userId));
    }
}
