package com.phincon.laza.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.model.dto.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage(), null);
        log.warn("AuthenticationException error: {}", e.getMessage());

        response.resetBuffer();
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(new ObjectMapper().writeValueAsString(errorResponse));
        response.flushBuffer();
    }
}
