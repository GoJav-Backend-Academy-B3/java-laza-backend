package com.phincon.laza.repository;

import com.phincon.laza.model.entity.PaymentMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PaymentMethodRepositoryTest {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    private PaymentMethod savedPaymentmethod;

    private PaymentMethod savedPaymentmethod2;
    @BeforeEach
    public void setUp() {
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

        savedPaymentmethod = paymentMethodRepository.save(paymentMethod1);
        savedPaymentmethod2 = paymentMethodRepository.save(paymentMethod2);
    }

    @AfterEach
    public void clearDB() {
        paymentMethodRepository.deleteAll();
    }

    @Test
    public void whenUserControllerInjected_thenNotNull() {
        assertThat(paymentMethodRepository).isNotNull();
    }

    @Test
    public void testFindAllByIsActiveIsTrue_thenFound() {
        Optional<PaymentMethod> result = paymentMethodRepository.findTop1ByNameAndIsActiveIsTrue("Sample Payment Method");

        assertNotNull(result.get());

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.get().getId());
        assertEquals("Sample Payment Method", result.get().getName());
        assertEquals("SAMPLE_CODE", result.get().getCode());
        assertEquals(true, result.get().getIsActive());
    }

    @Test
    public void testFindTop1ByNameAndIsActiveIsTrue() {
        Optional<PaymentMethod> result = paymentMethodRepository.findTop1ByNameAndIsActiveIsTrue("Sample Payment Method");

        assertNotNull(result.get());

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.get().getId());
        assertEquals("Sample Payment Method", result.get().getName());
        assertEquals("SAMPLE_CODE", result.get().getCode());
        assertEquals(true, result.get().getIsActive());
    }

}
