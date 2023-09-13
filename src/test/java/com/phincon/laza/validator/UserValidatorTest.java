package com.phincon.laza.validator;

import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.exception.custom.NotProcessException;
import com.phincon.laza.model.entity.EProvider;
import com.phincon.laza.model.entity.Provider;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.repository.ProviderRepository;
import com.phincon.laza.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserValidator userValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateUser_thenNotFound() {
        Optional<User> findUser = Optional.empty();
        lenient().when(userRepository.findById(anyString())).thenReturn(findUser);

        assertThrows(NotFoundException.class, () -> {
            userValidator.validateUserNotFound(findUser);
        });
    }

    @Test
    public void testValidateUser_thenUsernameExists() {
        Optional<User> findUser = Optional.of(new User());
        lenient().when(userRepository.findById(anyString())).thenReturn(findUser);

        assertThrows(ConflictException.class, () -> {
            userValidator.validateUserUsernameIsExists(findUser);
        });
    }

    @Test
    public void testValidateUser_thenEmailExists() {
        Optional<User> findUser = Optional.of(new User());
        lenient().when(userRepository.findById(anyString())).thenReturn(findUser);

        assertThrows(ConflictException.class, () -> {
            userValidator.validateUserUsernameIsExists(findUser);
        });
    }

    @Test
    public void testValidateUser_thenNotVerified() {
        Optional<User> findUser = Optional.of(new User());
        lenient().when(userRepository.findById(anyString())).thenReturn(findUser);

        assertThrows(BadRequestException.class, () -> {
            userValidator.validateUserNotIsVerified(findUser);
        });
    }

    @Test
    public void testValidateUser_thenBadCredentials() {
        User user = new User();
        user.setPassword(passwordEncoder.encode("pass"));

        Optional<User> findUser = Optional.of(user);
        lenient().when(userRepository.findById(anyString())).thenReturn(findUser);

        assertThrows(BadRequestException.class, () -> {
            userValidator.validateUserBadCredentials(findUser, "password");
        });
    }

    @Test
    public void testValidateUser_thenBadProviderLocal() {
        Optional<Provider> findProvider = providerRepository.findByName(EProvider.GOOGLE);
        Set<Provider> listProvider = new HashSet<>();
        findProvider.ifPresent(user -> listProvider.add(findProvider.get()));

        User user = new User();
        user.setProviders(listProvider);

        Optional<User> findUser = Optional.of(user);
        lenient().when(userRepository.findById(anyString())).thenReturn(findUser);

        assertThrows(NotProcessException.class, () -> {
            userValidator.validateUserBadProviderLocal(findUser);
        });
    }

    @Test
    public void testValidateUser_thenNotEqualProvider() {
        Optional<Provider> findProvider = providerRepository.findByName(EProvider.GOOGLE);
        Set<Provider> listProvider = new HashSet<>();
        findProvider.ifPresent(user -> listProvider.add(findProvider.get()));

        User user = new User();
        user.setProviders(listProvider);

        Optional<User> findUser = Optional.of(user);
        lenient().when(userRepository.findById(anyString())).thenReturn(findUser);

        assertThrows(NotProcessException.class, () -> {
            userValidator.validateUserNotEqualProvider(findUser, "local");
        });
    }

    @Test
    public void testValidateUser_thenPasswordNotMatch() {
        assertThrows(BadRequestException.class, () -> {
            userValidator.validateUserPasswordNotMatch("pass", "password");
        });
    }

    @Test
    public void testValidateUser_thenInvalidOldPassword() {
        assertThrows(BadRequestException.class, () -> {
            userValidator.validateUserInvalidOldPassword(passwordEncoder.encode("pass"), "password");
        });
    }
}
