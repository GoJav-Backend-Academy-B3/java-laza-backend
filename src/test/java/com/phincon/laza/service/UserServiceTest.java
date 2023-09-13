package com.phincon.laza.service;

import com.phincon.laza.model.dto.request.ChangePasswordRequest;
import com.phincon.laza.model.dto.request.RoleRequest;
import com.phincon.laza.model.dto.request.UserRequest;
import com.phincon.laza.model.entity.*;
import com.phincon.laza.repository.ProviderRepository;
import com.phincon.laza.repository.RoleRepository;
import com.phincon.laza.repository.UserRepository;
import com.phincon.laza.service.impl.UserServiceImpl;
import com.phincon.laza.validator.ProviderValidator;
import com.phincon.laza.validator.RoleValidator;
import com.phincon.laza.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleValidator roleValidator;

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private ProviderValidator providerValidator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void init() {
        Role role = new Role();
        role.setName(ERole.USER);

        Set<Role> listRole = new HashSet<>();
        listRole.add(role);

        Provider provider = new Provider();
        provider.setName(EProvider.LOCAL);

        Set<Provider> listProvider = new HashSet<>();
        listProvider.add(provider);

        User user = new User();
        user.setName("John Doe");
        user.setUsername("johndoe");
        user.setEmail("johndoe@mail.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles(listRole);
        user.setProviders(listProvider);

        List<User> listUser = new ArrayList<>();
        listUser.add(user);
        listUser.add(user);
        listUser.add(user);
        listUser.add(user);
        listUser.add(user);

        Pageable pageable = PageRequest.of(0, 5);

        lenient().when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(listUser, pageable, listUser.size()));
        lenient().when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        lenient().when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        lenient().when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        lenient().when(userRepository.save(any())).thenReturn(user);

        lenient().when(roleRepository.findByName(any())).thenReturn(Optional.of(role));
        lenient().when(providerRepository.findByName(any())).thenReturn(Optional.of(provider));
    }

    @Test
    public void testGetAllToUser_ThenCorrect() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<User> users = userService.getAll(pageable);

        assertNotNull(users);
        assertEquals(5, users.getSize());
        assertEquals(0, users.getNumber());
        assertEquals(5, users.getSize());
        assertEquals(1, users.getTotalPages());

        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testGetByIdToUser_ThenCorrect() {
        User user = userService.getById(anyString());

        assertNotNull(user);
        assertEquals("John Doe", user.getName());
        assertEquals("johndoe", user.getUsername());
        assertEquals("johndoe@mail.com", user.getEmail());

        verify(userRepository, times(1)).findById(anyString());
    }

    @Test
    public void testGetByUsernameToUser_ThenCorrect() {
        User user = userService.getByUsername(anyString());

        assertNotNull(user);
        assertEquals("John Doe", user.getName());
        assertEquals("johndoe", user.getUsername());
        assertEquals("johndoe@mail.com", user.getEmail());

        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    public void testUpdateToUser_thenCorrrect() throws Exception {
        UserRequest request = new UserRequest();
        request.setName("John Doe Update");
        request.setUsername("johndoeupdate");
        request.setEmail("johndoeupdate@mail.com");

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(new User()));
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        User user = userService.update(anyString(), request);

        assertNotNull(user);
        assertEquals(request.getName(), user.getName());
        assertEquals(request.getUsername(), user.getUsername());
        assertEquals(request.getEmail(), user.getEmail());

        verify(userRepository, times(1)).findById(anyString());
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testChangePasswordToUser_thenCorrect() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword(anyString());
        request.setNewPassword("password");
        request.setConfirmPassword("password");

        userService.changePassword(anyString(), request);

        verify(userRepository, times(1)).findById(anyString());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateRoleToUser_thenCorrect() {
        List<String> listRole = Arrays.asList("USER", "ADMIN");

        RoleRequest request = new RoleRequest();
        request.setUsername(anyString());
        request.setRoles(listRole);

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(new User()));

        userService.updateRole(request);

        verify(roleRepository, times(2)).findByName(any());
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, times(1)).save(any());
    }
}
