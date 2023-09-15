package com.phincon.laza.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.model.dto.request.ChangePasswordRequest;
import com.phincon.laza.model.dto.request.RoleRequest;
import com.phincon.laza.model.dto.request.UserRequest;
import com.phincon.laza.model.entity.*;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private SysUserDetails userDetails;

    @BeforeEach
    public void init() throws Exception {
        Role role = new Role();
        role.setName(ERole.USER);

        Set<Role> listRole = new HashSet<>();
        listRole.add(role);

        Provider provider = new Provider();
        provider.setName(EProvider.LOCAL);

        Set<Provider> listProvider = new HashSet<>();
        listProvider.add(provider);

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("John Doe");
        user.setUsername("johndoe");
        user.setEmail("johndoe@mail.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles(listRole);
        user.setProviders(listProvider);

        userDetails = SysUserDetails.create(user);

        List<User> listUser = new ArrayList<>();
        listUser.add(user);
        listUser.add(user);
        listUser.add(user);
        listUser.add(user);
        listUser.add(user);

        Pageable pageable = PageRequest.of(0, 5);

        lenient().when(userService.getAll(any())).thenReturn(new PageImpl<>(listUser, pageable, listUser.size()));
        lenient().when(userService.getById(any())).thenReturn(user);
        lenient().when(userService.update(anyString(), any())).thenReturn(user);
        doNothing().when(userService).changePassword(anyString(), any());
        doNothing().when(userService).updateRole(any());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testGetAllToUser_thenCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/management/users?page={page}&size={size}", 0, 5))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.metadata.page").value(0))
                .andExpect(jsonPath("$.metadata.size").value(5))
                .andExpect(jsonPath("$.metadata.count").value(1));

        verify(userService, times(1)).getAll(any());

        log.info("[COMPLETE] testing controller user getAll then correct");
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testGetAllToUser_thenForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/management/users?page={page}&size={size}", 0, 5))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.message").value("Access Denied"));

        verify(userService, times(0)).getAll(any());

        log.info("[COMPLETE] testing controller user getAll then forbidden");
    }

    @Test
    public void testProfileToUser_thenCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/me")
                        .with(user(userDetails))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(userService, times(1)).getById(anyString());

        log.info("[COMPLETE] testing controller user profile then correct");
    }

    @Test
    public void testUpdateToUser_thenCorrect() throws Exception {
        UserRequest request = new UserRequest();
        request.setName("John Doe");
        request.setUsername("johndoe");
        request.setEmail("johndoe@mail.com");

        mockMvc.perform(MockMvcRequestBuilders.put("/users/update")
                        .with(user(userDetails))
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .flashAttr("request", request))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(userService, times(1)).update(anyString(), any());

        log.info("[COMPLETE] testing controller user update then correct");
    }


    @Test
    public void testUpdateToUser_thenMethodInvalidArgsBlank() throws Exception {
        UserRequest request = new UserRequest();

        mockMvc.perform(MockMvcRequestBuilders.put("/users/update")
                        .with(user(userDetails))
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .flashAttr("request", request))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.name").value("name is required"))
                .andExpect(jsonPath("$.sub_error.username").value("username is required"))
                .andExpect(jsonPath("$.sub_error.email").value("email is required"));

        verify(userService, times(0)).update(anyString(), any());

        log.info("[COMPLETE] testing controller user update then method invalid arguments blank");
    }

    @Test
    public void testUpdateToUser_thenMethodInvalidArgsNotBlank() throws Exception {
        UserRequest request = new UserRequest();
        request.setName("John Doe1");
        request.setUsername("john1doe ");
        request.setEmail("johndoemail.com");

        mockMvc.perform(MockMvcRequestBuilders.put("/users/update")
                        .with(user(userDetails))
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .flashAttr("request", request))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.name").value("name must be alpha"))
                .andExpect(jsonPath("$.sub_error.username").value("username must be alphanumeric and not whitespace"))
                .andExpect(jsonPath("$.sub_error.email").value("input must be an email format"));

        verify(userService, times(0)).update(anyString(), any());

        log.info("[COMPLETE] testing controller user update then method invalid arguments not blank");
    }

    @Test
    public void testChangePasswordToUser_thenCorrect() throws Exception {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("password");
        request.setNewPassword("password");
        request.setConfirmPassword("password");

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/change-password")
                        .with(user(userDetails))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.name()));

        verify(userService, times(1)).changePassword(anyString(), any());

        log.info("[COMPLETE] testing controller user changePassword then correct");
    }

    @Test
    public void testChangePasswordToUser_thenMethodInvalidArgsBlank() throws Exception {
        ChangePasswordRequest request = new ChangePasswordRequest();

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/change-password")
                        .with(user(userDetails))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.newPassword").value("new password is required"))
                .andExpect(jsonPath("$.sub_error.confirmPassword").value("confirm password is required"));

        verify(userService, times(0)).changePassword(anyString(), any());

        log.info("[COMPLETE] testing controller user changePassword then method invalid arguments blank");
    }

    @Test
    public void testChangePasswordToUser_thenMethodInvalidArgsNotBlank() throws Exception {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setNewPassword("pass");
        request.setConfirmPassword("pass");

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/change-password")
                        .with(user(userDetails))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.newPassword").value("password must be minimum 8 characters"))
                .andExpect(jsonPath("$.sub_error.confirmPassword").value("password must be minimum 8 characters"));

        verify(userService, times(0)).changePassword(anyString(), any());

        log.info("[COMPLETE] testing controller user changePassword then method invalid arguments not blank");
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testUpdateRoleToUser_thenCorrect() throws Exception {
        RoleRequest request = new RoleRequest();
        request.setUsername("johndoe");
        request.setRoles(Arrays.asList("USER", "ADMIN"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/management/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.name()));

        verify(userService, times(1)).updateRole(any());

        log.info("[COMPLETE] testing controller user updateRole then correct");
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testUpdateRoleToUser_thenMethodInvalidArgsBlank() throws Exception {
        RoleRequest request = new RoleRequest();

        mockMvc.perform(MockMvcRequestBuilders.patch("/management/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.username").value("username is required"))
                .andExpect(jsonPath("$.sub_error.roles").value("roles is required"));

        verify(userService, times(0)).updateRole(any());

        log.info("[COMPLETE] testing controller user updateRole then method invalid arguments blank");
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testUpdateRoleToUser_thenMethodInvalidArgsNotBlank() throws Exception {
        RoleRequest request = new RoleRequest();
        request.setUsername("john1 doe");
        request.setRoles(Arrays.asList("USER", "ADMIN"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/management/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.username").value("username must be alphanumeric and not whitespace"));

        verify(userService, times(0)).updateRole(any());

        log.info("[COMPLETE] testing controller user updateRole then method invalid arguments not blank");
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void testUpdateRoleToUser_thenForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/management/users"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.message").value("Access Denied"));

        verify(userService, times(0)).updateRole(any());

        log.info("[COMPLETE] testing controller user updateRole then forbidden");
    }
}
