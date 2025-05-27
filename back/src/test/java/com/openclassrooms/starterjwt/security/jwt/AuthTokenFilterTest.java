package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class AuthTokenFilterTest {

    private final AuthTokenFilter authTokenFilter = new AuthTokenFilter();

    @Test
    void parseJwt_validToken_shouldReturnToken() {
        MockHttpServletRequest request = createRequestWithToken("validToken");

        String result = authTokenFilter.parseJwt(request);

        assertThat(result).isEqualTo("validToken");
    }

    @Test
    void parseJwt_noToken_shouldReturnNull() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        String result = authTokenFilter.parseJwt(request);

        assertThat(result).isNull();
    }

    @Test
    void parseJwt_invalidHeader_shouldReturnNull() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "InvalidHeader token");

        String result = authTokenFilter.parseJwt(request);

        assertThat(result).isNull();
    }

    private MockHttpServletRequest createRequestWithToken(String token) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        return request;
    }
}