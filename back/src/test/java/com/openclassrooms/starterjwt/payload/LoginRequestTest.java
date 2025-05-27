package com.openclassrooms.starterjwt.payload;


import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestTest {

    private Validator validator;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");
    }

    @Test
    void testValidLoginRequest() {
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    void testGettersAndSetters() {
        LoginRequest request = new LoginRequest();

        request.setEmail("user@test.com");
        request.setPassword("mypassword");

        assertThat(request.getEmail()).isEqualTo("user@test.com");
        assertThat(request.getPassword()).isEqualTo("mypassword");
    }

    @Test
    void testEmailNotBlank() {
        loginRequest.setEmail("");
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("must not be blank");
    }

    @Test
    void testEmailNotNull() {
        loginRequest.setEmail(null);
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("must not be blank");
    }

    @Test
    void testPasswordNotBlank() {
        loginRequest.setPassword("");
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("must not be blank");
    }

    @Test
    void testPasswordNotNull() {
        loginRequest.setPassword(null);
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("must not be blank");
    }

    @Test
    void testBothFieldsEmpty() {
        loginRequest.setEmail("");
        loginRequest.setPassword("");
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        assertThat(violations).hasSize(2);
    }

    @Test
    void testBothFieldsNull() {
        loginRequest.setEmail(null);
        loginRequest.setPassword(null);
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        assertThat(violations).hasSize(2);
    }

    @Test
    void testWhitespaceOnlyFields() {
        loginRequest.setEmail("   ");
        loginRequest.setPassword("   ");
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        assertThat(violations).hasSize(2);
    }

    @Test
    void testValidEmailFormats() {
        String[] validEmails = {
                "user@example.com",
                "test.email@domain.co.uk",
                "user123@test-domain.com"
        };

        for (String email : validEmails) {
            loginRequest.setEmail(email);
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
            assertThat(violations).isEmpty();
        }
    }

    @Test
    void testValidPasswords() {
        String[] validPasswords = {
                "password123",
                "P@ssw0rd!",
                "simplepass",
                "123456789"
        };

        for (String password : validPasswords) {
            loginRequest.setPassword(password);
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
            assertThat(violations).isEmpty();
        }
    }
}
