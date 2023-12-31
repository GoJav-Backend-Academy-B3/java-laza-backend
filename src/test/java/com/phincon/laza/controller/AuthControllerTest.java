package com.phincon.laza.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.exception.custom.BadRequestException;
import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.exception.custom.NotProcessException;
import com.phincon.laza.model.dto.request.*;
import com.phincon.laza.model.dto.response.TokenResponse;
import com.phincon.laza.model.entity.ERole;
import com.phincon.laza.model.entity.Role;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.security.jwt.JwtService;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.AuthService;
import com.phincon.laza.utils.GenerateRandom;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthController authController;

    @MockBean
    private AuthService authService;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    public void init() throws Exception {
        User user = new User();
        user.setName("John Doe");
        user.setUsername("johndoe");
        user.setEmail("johndoe@mail.com");
        user.setPassword("password");

        Role role = new Role();
        role.setName(ERole.USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        UserDetails userDetails = SysUserDetails.create(user);

        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);

        lenient().when(authService.login(any())).thenReturn(tokenResponse);
        lenient().when(authService.register(any())).thenReturn(user);
        doNothing().when(authService).registerResend(any());
        doNothing().when(authService).registerConfirm(anyString());
        doNothing().when(authService).forgotPassword(any());
        doNothing().when(authService).forgotPasswordConfirm(any());
        doNothing().when(authService).resetPassword(any());
        lenient().when(authService.refreshToken(anyString())).thenReturn(tokenResponse);
        lenient().when(authService.token(any())).thenReturn(tokenResponse);
    }

    @Test
    public void testAuthControllerInjected_thenNotNull() {
        assertThat(authController).isNotNull();
    }

    @Test
    public void testLoginRequestToAuth_thenCorrect() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("johndoe");
        request.setPassword("password");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(authService, times(1)).login(any());

        log.info("[COMPLETE] testing controller auth login then correct");
    }

    @Test
    public void testLoginRequestToAuth_thenMethodInvalidArgsBlank() throws Exception {
        LoginRequest request = new LoginRequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.username").value("username is required"))
                .andExpect(jsonPath("$.sub_error.password").value("password is required"));

        verify(authService, times(0)).login(any());

        log.info("[COMPLETE] testing controller auth login then method invalid arguments blank");
    }

    @Test
    public void testLoginRequestToAuth_thenMethodInvalidArgsNotBlank() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("john doe");
        request.setPassword("pass");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.username").value("username must be alphanumeric and not whitespace"))
                .andExpect(jsonPath("$.sub_error.password").value("password must be minimum 8 characters"));

        verify(authService, times(0)).login(any());

        log.info("[COMPLETE] testing controller auth login then method invalid argument not blank");
    }

    @Test
    public void testLoginRequestToAuth_thenNotFound() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("johndoe");
        request.setPassword("password");

        when(authService.login(request)).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()));

        verify(authService, times(1)).login(any());

        log.info("[COMPLETE] testing controller auth login then not found");
    }

    @Test
    public void testLoginRequestToAuth_thenBadRequest() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("johndoe");
        request.setPassword("password");

        when(authService.login(request)).thenThrow(BadRequestException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()));

        verify(authService, times(1)).login(any());

        log.info("[COMPLETE] testing controller auth login then bad request");
    }

    @Test
    public void testLoginRequestToAuth_thenNotProcess() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("johndoe");
        request.setPassword("password");

        when(authService.login(request)).thenThrow(NotProcessException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()));

        verify(authService, times(1)).login(any());

        log.info("[COMPLETE] testing controller auth login then not process entity");
    }

    @Test
    public void testRegisterRequestToAuth_thenCorrect() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setUsername("johndoe");
        request.setEmail("johndoe@mail.com");
        request.setPassword("password");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value(HttpStatus.CREATED.name()))
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(authService, times(1)).register(any());

        log.info("[COMPLETE] testing controller auth register then correct");
    }

    @Test
    public void testRegisterRequestToAuth_thenMethodInvalidArgsBlank() throws Exception {
        RegisterRequest request = new RegisterRequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.name").value("name is required"))
                .andExpect(jsonPath("$.sub_error.username").value("username is required"))
                .andExpect(jsonPath("$.sub_error.email").value("email is required"))
                .andExpect(jsonPath("$.sub_error.password").value("password is required"));

        verify(authService, times(0)).register(any());

        log.info("[COMPLETE] testing controller auth register then method invalid argument blank");
    }

    @Test
    public void testRegisterRequestToAuth_thenMethodInvalidArgsNotBlank() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe1");
        request.setUsername("john1doe ");
        request.setEmail("johndoemail.com");
        request.setPassword("pass");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.name").value("name must be alpha"))
                .andExpect(jsonPath("$.sub_error.username").value("username must be alphanumeric and not whitespace"))
                .andExpect(jsonPath("$.sub_error.email").value("input must be an email format"))
                .andExpect(jsonPath("$.sub_error.password").value("password must be minimum 8 characters"));

        verify(authService, times(0)).register(any());

        log.info("[COMPLETE] testing controller auth register then method invalid argument not blank");
    }

    @Test
    public void testRegisterRequestToAuth_thenNotFound() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setUsername("johndoe");
        request.setEmail("johndoe@mail.com");
        request.setPassword("password");

        when(authService.register(request)).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()));

        verify(authService, times(1)).register(any());

        log.info("[COMPLETE] testing controller auth register then not found");
    }

    @Test
    public void testRegisterRequestToAuth_thenConflict() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setUsername("johndoe");
        request.setEmail("johndoe@mail.com");
        request.setPassword("password");

        when(authService.register(request)).thenThrow(ConflictException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.CONFLICT.value()));

        verify(authService, times(1)).register(any());

        log.info("[COMPLETE] testing controller auth register then conflict entity");
    }

    @Test
    public void testRegisterResendRequestToAuth_thenCorrect() throws Exception {
        RecoveryRequest request = new RecoveryRequest();
        request.setEmail("johndoe@mail.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register/resend")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.name()));

        verify(authService, times(1)).registerResend(any());

        log.info("[COMPLETE] testing controller auth registerResend then correct");
    }

    @Test
    public void testRegisterResendRequestToAuth_thenMethodInvalidArgsBlank() throws Exception {
        RecoveryRequest request = new RecoveryRequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register/resend")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.email").value("email is required"));

        verify(authService, times(0)).registerResend(any());

        log.info("[COMPLETE] testing controller auth registerResend then method invalid argument blank");
    }

    @Test
    public void testRegisterResendRequestToAuth_thenMethodInvalidArgsNotBlank() throws Exception {
        RecoveryRequest request = new RecoveryRequest();
        request.setEmail("johndoemail.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register/resend")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.email").value("input must be an email format"));

        verify(authService, times(0)).registerResend(any());

        log.info("[COMPLETE] testing controller auth registerResend then method invalid argument not blank");
    }

    @Test
    public void testRegisterResendRequestToAuth_thenNotFound() throws Exception {
        RecoveryRequest request = new RecoveryRequest();
        request.setEmail("johndoe@mail.com");

        doThrow(NotFoundException.class).when(authService).registerResend(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register/resend")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()));

        verify(authService, times(1)).registerResend(any());

        log.info("[COMPLETE] testing controller auth registerResend then not found");
    }

    @Test
    public void testRegisterConfirmRequestToAuth_thenCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/register/confirm?token={token}", anyString())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.name()));

        verify(authService, times(1)).registerConfirm(anyString());

        log.info("[COMPLETE] testing controller auth registerConfirm then correct");
    }

    @Test
    public void testRegisterConfirmRequestToAuth_thenNotFound() throws Exception {
        String token = "random token";

        doThrow(NotFoundException.class).when(authService).registerConfirm(token);

        mockMvc.perform(MockMvcRequestBuilders.get("/auth/register/confirm?token={token}", token)
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()));

        verify(authService, times(1)).registerConfirm(anyString());

        log.info("[COMPLETE] testing controller auth registerConfirm then not found");
    }

    @Test
    public void testRegisterConfirmRequestToAuth_thenNotProcess() throws Exception {
        String token = "random token";

        doThrow(NotProcessException.class).when(authService).registerConfirm(token);

        mockMvc.perform(MockMvcRequestBuilders.get("/auth/register/confirm?token={token}", token)
                        .with(csrf()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()));

        verify(authService, times(1)).registerConfirm(anyString());

        log.info("[COMPLETE] testing controller auth registerConfirm then not process");
    }

    @Test
    public void testForgotPasswordRequestToAuth_thenCorrect() throws Exception {
        RecoveryRequest request = new RecoveryRequest();
        request.setEmail("johndoe@mail.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/forgot-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.name()));

        verify(authService, times(1)).forgotPassword(any());

        log.info("[COMPLETE] testing controller auth forgotPassword then correct");
    }

    @Test
    public void testForgotPasswordRequestToAuth_thenMethodInvalidArgsBlank() throws Exception {
        RecoveryRequest request = new RecoveryRequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/forgot-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.email").value("email is required"));

        verify(authService, times(0)).forgotPassword(any());

        log.info("[COMPLETE] testing controller auth forgotPassword then method invalid argument blank");
    }

    @Test
    public void testForgotPasswordRequestToAuth_thenMethodInvalidArgsNotBlank() throws Exception {
        RecoveryRequest request = new RecoveryRequest();
        request.setEmail("johndoemail.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/forgot-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.email").value("input must be an email format"));

        verify(authService, times(0)).forgotPassword(any());

        log.info("[COMPLETE] testing controller auth forgotPassword then method invalid argument not blank");
    }

    @Test
    public void testForgotPasswordRequestToAuth_thenNotFound() throws Exception {
        RecoveryRequest request = new RecoveryRequest();
        request.setEmail("johndoe@mail.com");

        doThrow(NotFoundException.class).when(authService).forgotPassword(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/forgot-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()));

        verify(authService, times(1)).forgotPassword(any());

        log.info("[COMPLETE] testing controller auth forgotPassword then not found");
    }

    @Test
    public void testForgotPasswordRequestToAuth_thenNotProcess() throws Exception {
        RecoveryRequest request = new RecoveryRequest();
        request.setEmail("johndoe@mail.com");

        doThrow(NotProcessException.class).when(authService).forgotPassword(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/forgot-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()));

        verify(authService, times(1)).forgotPassword(any());

        log.info("[COMPLETE] testing controller auth forgotPassword then not process");
    }

    @Test
    public void testForgotPasswordConfirmRequestToAuth_thenCorrect() throws Exception {
        VerificationCodeRequest request = new VerificationCodeRequest();
        request.setCode(GenerateRandom.code());
        request.setEmail("johndoe@mail.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/forgot-password/confirm")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.name()));

        verify(authService, times(1)).forgotPasswordConfirm(any());

        log.info("[COMPLETE] testing controller auth forgotPasswordConfirm then correct");
    }

    @Test
    public void testForgotPasswordConfirmRequestToAuth_thenMethodInvalidArgsBlank() throws Exception {
        VerificationCodeRequest request = new VerificationCodeRequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/forgot-password/confirm")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.code").value("code is required"))
                .andExpect(jsonPath("$.sub_error.email").value("email is required"));

        verify(authService, times(0)).forgotPasswordConfirm(any());

        log.info("[COMPLETE] testing controller auth forgotPasswordConfirm then method invalid argument blank");
    }

    @Test
    public void testForgotPasswordConfirmRequestToAuth_thenMethodInvalidArgsNotBlank() throws Exception {
        VerificationCodeRequest request = new VerificationCodeRequest();
        request.setCode(GenerateRandom.token());
        request.setEmail("johndoemail.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/forgot-password/confirm")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.code").value("code must be minimum 4 characters"))
                .andExpect(jsonPath("$.sub_error.email").value("input must be an email format"));

        verify(authService, times(0)).forgotPasswordConfirm(any());

        log.info("[COMPLETE] testing controller auth forgotPasswordConfirm then method invalid argument not blank");
    }

    @Test
    public void testForgotPasswordConfirmRequestToAuth_thenNotFound() throws Exception {
        VerificationCodeRequest request = new VerificationCodeRequest();
        request.setCode(GenerateRandom.code());
        request.setEmail("johndoe@mail.com");

        doThrow(NotFoundException.class).when(authService).forgotPasswordConfirm(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/forgot-password/confirm")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()));

        verify(authService, times(1)).forgotPasswordConfirm(any());

        log.info("[COMPLETE] testing controller auth forgotPasswordConfirm then not found");
    }

    @Test
    public void testForgotPasswordConfirmRequestToAuth_thenNotProcess() throws Exception {
        VerificationCodeRequest request = new VerificationCodeRequest();
        request.setCode(GenerateRandom.code());
        request.setEmail("johndoe@mail.com");

        doThrow(NotProcessException.class).when(authService).forgotPasswordConfirm(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/forgot-password/confirm")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()));

        verify(authService, times(1)).forgotPasswordConfirm(any());

        log.info("[COMPLETE] testing controller auth forgotPasswordConfirm then not process");
    }

    @Test
    public void testResetPasswordRequestToAuth_thenCorrect() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setCode(GenerateRandom.code());
        request.setEmail("johndoe@mail.com");
        request.setNewPassword("password");
        request.setConfirmPassword("password");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/reset-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.name()));

        verify(authService, times(1)).resetPassword(any());

        log.info("[COMPLETE] testing controller auth resetPassword then correct");
    }

    @Test
    public void testResetPasswordRequestToAuth_thenMethodInvalidArgsBlank() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/reset-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.code").value("code is required"))
                .andExpect(jsonPath("$.sub_error.email").value("email is required"))
                .andExpect(jsonPath("$.sub_error.newPassword").value("new password is required"))
                .andExpect(jsonPath("$.sub_error.confirmPassword").value("confirm password is required"));

        verify(authService, times(0)).resetPassword(any());

        log.info("[COMPLETE] testing controller auth resetPassword then method invalid argument blank");
    }

    @Test
    public void testResetPasswordRequestToAuth_thenMethodInvalidArgsNotBlank() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setCode(GenerateRandom.token());
        request.setEmail("johndoemail.com");
        request.setNewPassword("pass");
        request.setConfirmPassword("pass");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/reset-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Error validation"))
                .andExpect(jsonPath("$.sub_error").isMap())
                .andExpect(jsonPath("$.sub_error.code").value("code must be minimum 4 characters"))
                .andExpect(jsonPath("$.sub_error.email").value("input must be an email format"))
                .andExpect(jsonPath("$.sub_error.newPassword").value("password must be minimum 8 characters"))
                .andExpect(jsonPath("$.sub_error.confirmPassword").value("password must be minimum 8 characters"));

        verify(authService, times(0)).resetPassword(any());

        log.info("[COMPLETE] testing controller auth resetPassword then method invalid argument not blank");
    }

    @Test
    public void testResetPasswordRequestToAuth_thenNotFound() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setCode(GenerateRandom.code());
        request.setEmail("johndoe@mail.com");
        request.setNewPassword("password");
        request.setConfirmPassword("password");

        doThrow(NotFoundException.class).when(authService).resetPassword(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/reset-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()));

        verify(authService, times(1)).resetPassword(any());

        log.info("[COMPLETE] testing controller auth resetPassword then not found");
    }

    @Test
    public void testResetPasswordRequestToAuth_thenNotProcess() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setCode(GenerateRandom.code());
        request.setEmail("johndoe@mail.com");
        request.setNewPassword("password");
        request.setConfirmPassword("password");

        doThrow(NotProcessException.class).when(authService).resetPassword(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/reset-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()));

        verify(authService, times(1)).resetPassword(any());

        log.info("[COMPLETE] testing controller auth resetPassword then not process");
    }

    @Test
    public void testRefreshTokenRequestToAuth_thenCorrect() throws Exception {
        String authHeader = "Bearer valid_refresh_token";

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh-token")
                        .with(csrf())
                        .header("X-AUTH-REFRESH", authHeader))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.data").isNotEmpty());

        verify(authService, times(1)).refreshToken(anyString());

        log.info("[COMPLETE] testing controller auth refreshToken then correct");
    }

    @Test
    public void testRefreshTokenRequestToAuth_thenNotFound() throws Exception {
        String authHeader = "Bearer valid_refresh_token";

        doThrow(NotFoundException.class).when(authService).refreshToken(authHeader);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh-token")
                        .with(csrf())
                        .header("X-AUTH-REFRESH", authHeader))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.NOT_FOUND.value()));

        verify(authService, times(1)).refreshToken(anyString());

        log.info("[COMPLETE] testing controller auth refreshToken then not found");
    }

    @Test
    public void testRefreshTokenRequestToAuth_thenNotProcess() throws Exception {
        String authHeader = "Bearer valid_refresh_token";

        doThrow(NotProcessException.class).when(authService).refreshToken(authHeader);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh-token")
                        .with(csrf())
                        .header("X-AUTH-REFRESH", authHeader))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()));

        verify(authService, times(1)).refreshToken(anyString());

        log.info("[COMPLETE] testing controller auth refreshToken then not process");
    }
}
