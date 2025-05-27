package com.openclassrooms.starterjwt.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.MimeTypeUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class AuthEntryPointJwtTest {

    private final AuthEntryPointJwt authEntryPointJwt = new AuthEntryPointJwt();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void commence_shouldReturnUnauthorizedResponse() throws IOException, ServletException {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new AuthenticationException("Unauthorized error message") {};

        // When
        authEntryPointJwt.commence(request, response, authException);

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(response.getContentType()).isEqualTo(MimeTypeUtils.APPLICATION_JSON_VALUE);

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) objectMapper.readValue(response.getContentAsString(), Map.class);
        assertThat(responseBody.get("status")).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(responseBody.get("error")).isEqualTo("Unauthorized");
        assertThat(responseBody.get("message")).isEqualTo("Unauthorized error message");
        assertThat(responseBody.get("path")).isEqualTo(request.getServletPath());
    }
}