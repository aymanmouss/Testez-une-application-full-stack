package com.openclassrooms.starterjwt.payload;



import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SignupRequestTest {

    private Validator validator;
    private SignupRequest signupRequest;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");
    }

    @Test
    void testValidSignupRequest() {
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    void testGettersAndSetters() {
        SignupRequest request = new SignupRequest();

        request.setEmail("user@test.com");
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setPassword("mypassword");

        assertThat(request.getEmail()).isEqualTo("user@test.com");
        assertThat(request.getFirstName()).isEqualTo("Jane");
        assertThat(request.getLastName()).isEqualTo("Smith");
        assertThat(request.getPassword()).isEqualTo("mypassword");
    }

    @Test
    void testEmailNotBlank() {
        signupRequest.setEmail("");
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("must not be blank");
    }

    @Test
    void testEmailInvalid() {
        signupRequest.setEmail("invalid-email");
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("must be a well-formed email address");
    }

    @Test
    void testEmailMaxSize() {
        signupRequest.setEmail("a".repeat(45) + "@test.com");
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("size must be between");
    }

    @Test
    void testEmailValidMaxSize() {
        signupRequest.setEmail("a".repeat(40) + "@test.com");
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        assertThat(violations).isEmpty();
    }

    @Test
    void testFirstNameNotBlank() {
        signupRequest.setFirstName("");
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        assertThat(violations).hasSize(2);
        boolean hasNotBlankError = violations.stream().anyMatch(v -> v.getMessage().contains("must not be blank"));
        boolean hasSizeError = violations.stream().anyMatch(v -> v.getMessage().contains("size must be between"));
        assertThat(hasNotBlankError).isTrue();
        assertThat(hasSizeError).isTrue();
    }

    @Test
    void testFirstNameMinSize() {
        signupRequest.setFirstName("Jo");
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("size must be between");
    }

    @Test
    void testFirstNameMaxSize() {
        signupRequest.setFirstName("A".repeat(21));
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("size must be between");
    }

    @Test
    void testFirstNameValidSizes() {
        signupRequest.setFirstName("Bob");
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertThat(violations).isEmpty();

        signupRequest.setFirstName("A".repeat(20));
        violations = validator.validate(signupRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    void testLastNameNotBlank() {
        signupRequest.setLastName("");
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        assertThat(violations).hasSize(2);
        boolean hasNotBlankError = violations.stream().anyMatch(v -> v.getMessage().contains("must not be blank"));
        boolean hasSizeError = violations.stream().anyMatch(v -> v.getMessage().contains("size must be between"));
        assertThat(hasNotBlankError).isTrue();
        assertThat(hasSizeError).isTrue();
    }

    @Test
    void testLastNameMinSize() {
        signupRequest.setLastName("Do");
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("size must be between");
    }

    @Test
    void testLastNameMaxSize() {
        signupRequest.setLastName("A".repeat(21));
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("size must be between");
    }

    @Test
    void testLastNameValidSizes() {
        signupRequest.setLastName("Lee");
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertThat(violations).isEmpty();

        signupRequest.setLastName("A".repeat(20));
        violations = validator.validate(signupRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    void testPasswordNotBlank() {
        signupRequest.setPassword("");
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        assertThat(violations).hasSize(2);
        boolean hasNotBlankError = violations.stream().anyMatch(v -> v.getMessage().contains("must not be blank"));
        boolean hasSizeError = violations.stream().anyMatch(v -> v.getMessage().contains("size must be between"));
        assertThat(hasNotBlankError).isTrue();
        assertThat(hasSizeError).isTrue();
    }

    @Test
    void testPasswordMinSize() {
        signupRequest.setPassword("12345");
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("size must be between");
    }

    @Test
    void testPasswordMaxSize() {
        signupRequest.setPassword("A".repeat(41));
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("size must be between");
    }

    @Test
    void testPasswordValidSizes() {
        signupRequest.setPassword("123456");
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertThat(violations).isEmpty();

        signupRequest.setPassword("A".repeat(40));
        violations = validator.validate(signupRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    void testMultipleValidationErrors() {
        SignupRequest invalidRequest = new SignupRequest();
        invalidRequest.setEmail("invalid");
        invalidRequest.setFirstName("Jo");
        invalidRequest.setLastName("Do");
        invalidRequest.setPassword("12345");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(invalidRequest);
        assertThat(violations).hasSize(4);
    }

    @Test
    void testAllFieldsNull() {
        SignupRequest nullRequest = new SignupRequest();

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(nullRequest);
        assertThat(violations).hasSize(4);
    }

    @Test
    void testValidEmails() {
        String[] validEmails = {
                "user@example.com",
                "test.email@domain.co.uk",
                "user123@test-domain.com"
        };

        for (String email : validEmails) {
            signupRequest.setEmail(email);
            Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
            assertThat(violations).isEmpty();
        }
    }

    @Test
    void testEqualsAndHashCode() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@test.com");
        request1.setFirstName("John");
        request1.setLastName("Doe");
        request1.setPassword("password");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@test.com");
        request2.setFirstName("John");
        request2.setLastName("Doe");
        request2.setPassword("password");

        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }

    @Test
    void testToString() {
        String requestString = signupRequest.toString();

        assertThat(requestString).contains("SignupRequest");
        assertThat(requestString).contains("email=test@example.com");
        assertThat(requestString).contains("firstName=John");
        assertThat(requestString).contains("lastName=Doe");
    }
}