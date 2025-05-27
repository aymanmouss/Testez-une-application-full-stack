package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private Validator validator;
    private User user;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .admin(false)
                .build();
    }

    @Test
    void testValidUser() {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isEmpty();
    }

    @Test
    void testUserBuilder() {
        User builtUser = User.builder()
                .id(1L)
                .email("user@test.com")
                .firstName("Jane")
                .lastName("Smith")
                .password("password456")
                .admin(true)
                .build();

        assertThat(builtUser.getId()).isEqualTo(1L);
        assertThat(builtUser.getEmail()).isEqualTo("user@test.com");
        assertThat(builtUser.getFirstName()).isEqualTo("Jane");
        assertThat(builtUser.getLastName()).isEqualTo("Smith");
        assertThat(builtUser.getPassword()).isEqualTo("password456");
        assertThat(builtUser.isAdmin()).isTrue();
    }

    @Test
    void testNoArgsConstructor() {
        User emptyUser = new User();
        assertThat(emptyUser).isNotNull();
        assertThat(emptyUser.getId()).isNull();
        assertThat(emptyUser.getEmail()).isNull();
        assertThat(emptyUser.getFirstName()).isNull();
        assertThat(emptyUser.getLastName()).isNull();
        assertThat(emptyUser.getPassword()).isNull();
    }

    @Test
    void testRequiredArgsConstructor() {
        User requiredUser = new User("user@test.com", "Smith", "Jane", "password789", true);

        assertThat(requiredUser.getEmail()).isEqualTo("user@test.com");
        assertThat(requiredUser.getLastName()).isEqualTo("Smith");
        assertThat(requiredUser.getFirstName()).isEqualTo("Jane");
        assertThat(requiredUser.getPassword()).isEqualTo("password789");
        assertThat(requiredUser.isAdmin()).isTrue();
        assertThat(requiredUser.getId()).isNull();
    }

    @Test
    void testGettersAndSetters() {
        User testUser = new User();

        testUser.setId(2L);
        testUser.setEmail("alice@test.com");
        testUser.setFirstName("Alice");
        testUser.setLastName("Johnson");
        testUser.setPassword("newpassword");
        testUser.setAdmin(true);

        assertThat(testUser.getId()).isEqualTo(2L);
        assertThat(testUser.getEmail()).isEqualTo("alice@test.com");
        assertThat(testUser.getFirstName()).isEqualTo("Alice");
        assertThat(testUser.getLastName()).isEqualTo("Johnson");
        assertThat(testUser.getPassword()).isEqualTo("newpassword");
        assertThat(testUser.isAdmin()).isTrue();
    }

    @Test
    void testChainedAccessors() {
        User chainedUser = new User()
                .setId(3L)
                .setEmail("bob@test.com")
                .setFirstName("Bob")
                .setLastName("Wilson")
                .setPassword("bobpassword")
                .setAdmin(false);

        assertThat(chainedUser.getId()).isEqualTo(3L);
        assertThat(chainedUser.getEmail()).isEqualTo("bob@test.com");
        assertThat(chainedUser.getFirstName()).isEqualTo("Bob");
        assertThat(chainedUser.getLastName()).isEqualTo("Wilson");
        assertThat(chainedUser.getPassword()).isEqualTo("bobpassword");
        assertThat(chainedUser.isAdmin()).isFalse();
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = User.builder().id(1L).email("test1@test.com").firstName("John").lastName("Doe").password("pass").admin(false).build();
        User user2 = User.builder().id(1L).email("test2@test.com").firstName("Jane").lastName("Smith").password("pass").admin(true).build();
        User user3 = User.builder().id(2L).email("test1@test.com").firstName("John").lastName("Doe").password("pass").admin(false).build();

        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
        assertThat(user1).isNotEqualTo(user3);
    }

    @Test
    void testToString() {
        String userString = user.toString();

        assertThat(userString).contains("User");
        assertThat(userString).contains("id=1");
        assertThat(userString).contains("email=test@example.com");
        assertThat(userString).contains("firstName=John");
        assertThat(userString).contains("lastName=Doe");
        assertThat(userString).contains("admin=false");
    }

    @Test
    void testEmailValidation() {
        user.setEmail("invalid-email");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("must be a well-formed email address");
    }

    @Test
    void testEmailMaxSize() {
        user.setEmail("a".repeat(45) + "@test.com");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("size must be between");
    }

    @Test
    void testEmailValidMaxSize() {
        user.setEmail("a".repeat(40) + "@test.com");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).isEmpty();
    }

    @Test
    void testFirstNameMaxSize() {
        user.setFirstName("A".repeat(21));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("size must be between");
    }

    @Test
    void testFirstNameValidMaxSize() {
        user.setFirstName("A".repeat(20));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).isEmpty();
    }

    @Test
    void testLastNameMaxSize() {
        user.setLastName("A".repeat(21));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("size must be between");
    }

    @Test
    void testLastNameValidMaxSize() {
        user.setLastName("A".repeat(20));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).isEmpty();
    }

    @Test
    void testPasswordMaxSize() {
        user.setPassword("A".repeat(121));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("size must be between");
    }

    @Test
    void testPasswordValidMaxSize() {
        user.setPassword("A".repeat(120));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).isEmpty();
    }

    @Test
    void testAdminField() {
        user.setAdmin(true);
        assertThat(user.isAdmin()).isTrue();

        user.setAdmin(false);
        assertThat(user.isAdmin()).isFalse();
    }

    @Test
    void testMultipleValidationErrors() {
        User invalidUser = User.builder()
                .email("invalid-email")
                .firstName("A".repeat(21))
                .lastName("B".repeat(21))
                .password("C".repeat(121))
                .admin(false)
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(invalidUser);
        assertThat(violations).hasSize(4);
    }

    @Test
    void testTimestampFields() {
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();

        user.setCreatedAt(createdAt);
        user.setUpdatedAt(updatedAt);

        assertThat(user.getCreatedAt()).isEqualTo(createdAt);
        assertThat(user.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(user.getUpdatedAt()).isAfter(user.getCreatedAt());
    }

    @Test
    void testValidEmails() {
        String[] validEmails = {
                "user@example.com",
                "test.email@domain.co.uk",
                "user123@test-domain.com",
                "user+tag@example.org"
        };

        for (String email : validEmails) {
            user.setEmail(email);
            Set<ConstraintViolation<User>> violations = validator.validate(user);
            assertThat(violations).isEmpty();
        }
    }

    @Test
    void testInvalidEmails() {
        String[] invalidEmails = {
                "invalid-email",
                "@example.com",
                "user@",
                "user..name@example.com"
        };

        for (String email : invalidEmails) {
            user.setEmail(email);
            Set<ConstraintViolation<User>> violations = validator.validate(user);
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).contains("must be a well-formed email address");
        }
    }
}