package com.phincon.laza.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.dto.response.TokenResponse;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        TokenResponse token = authService.token((SysUserDetails) authentication.getPrincipal());

        DataResponse<TokenResponse> dataResponse = new DataResponse<>(HttpStatus.OK.value(), HttpStatus.OK.name(), token, null);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), dataResponse);

        clearAuthenticationAttributes(request, response);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
    }
}
