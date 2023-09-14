package com.phincon.laza.repository;

import com.phincon.laza.model.entity.EProvider;
import com.phincon.laza.model.entity.Provider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProviderRepositoryTest {
    @Autowired
    private ProviderRepository providerRepository;

    @Test
    public void testSaveAndFindByNameToProvider_thenCorrect() {
        Provider provider = new Provider();
        provider.setName(EProvider.LOCAL);

        providerRepository.save(provider);

        Optional<Provider> findProvider = providerRepository.findByName(EProvider.LOCAL);

        assertTrue(findProvider.isPresent());
        assertEquals(findProvider.get().getName(), provider.getName());

        log.info("[COMPLETE] testing repository provider findByName then correct");
    }
}
