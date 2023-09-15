package com.phincon.laza.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.phincon.laza.config.CreditCardDataConfig;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.CreditCard;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.CreditCardService;

@SpringBootTest
@AutoConfigureMockMvc
@SpringJUnitConfig({ CreditCardDataConfig.class })
public class CreditCardControllerTest {

    @MockBean
    private CreditCardService service;

    @InjectMocks
    private CreditCardController controller;

    @Autowired
    private MockMvc mockmvc;

    @Autowired
    @Qualifier("cc.all")
    private List<CreditCard> creditCards;

    @Autowired
    @Qualifier("cc.one")
    private CreditCard creditCardOne;

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

    @Test
    @DisplayName("get one credit card should return data if found")
    public void getOneCCUser_data() throws Exception {
        SysUserDetails ud = new SysUserDetails("hahah", "asdf", "ghjkl",
                Arrays.asList(new SimpleGrantedAuthority("USER")));
        final CreditCard creditCard = creditCardOne;
        final String id = creditCard.getId();
        Mockito.when(service.getById(id)).thenReturn(creditCard);

        var action = mockmvc.perform(MockMvcRequestBuilders.get("/cc/{id}", id).with(user(ud)));

        action.andExpectAll(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$.data").exists(),
                MockMvcResultMatchers.jsonPath("$.data.card_number").value(creditCard.getCardNumber()),
                MockMvcResultMatchers.jsonPath("$.data.expiry_month").value(creditCard.getExpiryMonth()),
                MockMvcResultMatchers.jsonPath("$.data.expiry_year").value(creditCard.getExpiryYear()));

        Mockito.verify(service, Mockito.times(1)).getById(Mockito.eq(id));
    }

    @Test
    @DisplayName("get one credit card should return not found")
    public void getOneCCUser_notfound() throws Exception {
        SysUserDetails ud = new SysUserDetails("hahah", "asdf", "ghjkl",
                Arrays.asList(new SimpleGrantedAuthority("USER")));
        final String id = "cc123";
        Mockito.when(service.getById(Mockito.anyString())).thenThrow(NotFoundException.class);

        var action = mockmvc.perform(MockMvcRequestBuilders.get("/cc/{id}", id).with(user(ud)));

        action.andExpectAll(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                MockMvcResultMatchers.status().isNotFound());
    }
}
