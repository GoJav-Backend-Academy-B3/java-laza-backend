package com.phincon.laza.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.phincon.laza.config.CreditCardDataConfig;
import com.phincon.laza.model.entity.CreditCard;
import com.phincon.laza.repository.CreditCardRepository;
import com.phincon.laza.service.impl.CreditCardServiceImpl;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringJUnitConfig({CreditCardDataConfig.class})
public class CreditCardServiceTest {
    @Mock
    private CreditCardRepository repository;

    @InjectMocks
    private final CreditCardService service = new CreditCardServiceImpl();

    @Autowired
    @Qualifier("cc.all")
    private List<CreditCard> creditCards;

    @Autowired
    @Qualifier("cc.one")
    private CreditCard creditCardOne;

    @Test
    @DisplayName("get all credit card should list all credit cards")
    public void getAllCreditCards_data() {
        final String userId = "userid1";
        Mockito.when(repository.findAllByUserId(Mockito.eq(userId))).thenReturn(creditCards);

        var lists = service.getAll("userid1");
        
        Assertions.assertTrue(CollectionUtils.isEqualCollection(creditCards, lists));
        Mockito.verify(repository, Mockito.times(1)).findAllByUserId(userId);
    }

    @Test
    @DisplayName("get one credit card should return data")
    public void getOneCreditCards_data() {
        final CreditCard creditCard = creditCardOne;
        final String id = creditCard.getId();
        Mockito.when(repository.findById(Mockito.anyString())).thenReturn(Optional.of(creditCard));
   
        var result = service.getById(id);

        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(creditCard.getCardNumber(), result.getCardNumber());
        Assertions.assertEquals(creditCard.getExpiryYear(), result.getExpiryYear());
        Mockito.verify(repository, Mockito.times(1)).findById(id);
    }
}
