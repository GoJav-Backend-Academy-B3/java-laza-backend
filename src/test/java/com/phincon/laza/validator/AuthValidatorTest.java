package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.entity.ERole;
import com.phincon.laza.model.entity.Role;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.security.jwt.JwtService;
import com.phincon.laza.security.userdetails.SysUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AuthValidatorTest {
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthValidator authValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateAuth_thenHeaderNotFound() {
        assertThrows(NotFoundException.class, () -> {
            authValidator.validateAuthHeaderNotFound("random header");
        });

        log.info("[COMPLETE] testing validate auth then header not found");
    }

    @Test
    public void testValidateAuth_thenTokenInvalid() {
        Role role = new Role();
        role.setName(ERole.USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user1 = new User();
        user1.setName("John Doe");
        user1.setUsername("johndoe");
        user1.setEmail("johndoe@mail.com");
        user1.setPassword("password");
        user1.setRoles(roles);

        UserDetails user1Details = SysUserDetails.create(user1);

        User user2 = new User();
        user2.setName("Alice Smith");
        user2.setUsername("alicesmith");
        user2.setEmail("alicesmith@mail.com");
        user2.setPassword("password");
        user2.setRoles(roles);

        UserDetails user2Details = SysUserDetails.create(user2);
        String refreshToken = jwtService.generateRefreshToken(user2Details);

        assertThrows(BadRequestException.class, () -> {
            authValidator.validateAuthTokenInvalid(String.format("Bearer %s", refreshToken), user1Details);
        });

        log.info("[COMPLETE] testing validate auth then token invalid");
    }

    @Test
    public void testValidateAuth_thenUsernameNull() {
        assertThrows(NotFoundException.class, () -> {
            authValidator.validateAuthUsernameNull(null);
        });

        log.info("[COMPLETE] testing validate auth then username null");
    }

    @Test
    public void testValidateAuth_thenEmailNull() {
        assertThrows(AuthenticationException.class, () -> {
            authValidator.validateAuthEmailNull(null);
        });

        log.info("[COMPLETE] testing validate auth then email null");
    }
}
