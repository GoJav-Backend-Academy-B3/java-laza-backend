package com.phincon.laza.repository;

import com.phincon.laza.model.entity.Provider;
import com.phincon.laza.model.entity.Role;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.model.entity.VerificationToken;
import com.phincon.laza.utils.GenerateRandom;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VerificationTokenRepositoryTest {
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testSaveAndFindByTokenToVerificationToken_thenCorrect() {
        Set<Role> listRole = new HashSet<>();
        Set<Provider> listProvider = new HashSet<>();

        User user = new User();
        user.setName("John Doe");
        user.setUsername("johndoe");
        user.setEmail("johndoe@mail.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles(listRole);
        user.setProviders(listProvider);

        userRepository.save(user);

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(GenerateRandom.token());
        verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);

        Optional<VerificationToken> findToken = verificationTokenRepository.findByToken(verificationToken.getToken());

        assertTrue(findToken.isPresent());
        assertEquals(findToken.get().getToken(), verificationToken.getToken());
        assertEquals(findToken.get().getUser().getId(), user.getId());
    }
}
