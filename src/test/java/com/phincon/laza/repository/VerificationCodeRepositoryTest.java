package com.phincon.laza.repository;

import com.phincon.laza.model.entity.Provider;
import com.phincon.laza.model.entity.Role;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.model.entity.VerificationCode;
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
public class VerificationCodeRepositoryTest {
    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testSaveAndFindByCodeAndUserIdToVerificationCode_thenCorrect() {
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

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setCode(GenerateRandom.code());
        verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        verificationCode.setUser(user);

        verificationCodeRepository.save(verificationCode);

        Optional<VerificationCode> findCode = verificationCodeRepository.findByCodeAndUserId(verificationCode.getCode(), user.getId());

        assertTrue(findCode.isPresent());
        assertEquals(findCode.get().getCode(), verificationCode.getCode());
        assertEquals(findCode.get().getUser().getId(), user.getId());
    }
}
