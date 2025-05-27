package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SessionTest {

    private Validator validator;
    private Session session;
    private Teacher teacher;
    private User user1, user2;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        user1 = User.builder()
                .id(1L)
                .email("user1@test.com")
                .firstName("User")
                .lastName("One")
                .password("password123")
                .admin(false)
                .build();

        user2 = User.builder()
                .id(2L)
                .email("user2@test.com")
                .firstName("User")
                .lastName("Two")
                .password("password456")
                .admin(false)
                .build();

        session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(new Date())
                .description("A relaxing yoga session for beginners")
                .teacher(teacher)
                .users(new ArrayList<>(List.of(user1, user2)))
                .build();
    }

    @Test
    void testValidSession() {
        Set<ConstraintViolation<Session>> violations = validator.validate(session);
        assertThat(violations).isEmpty();
    }

    @Test
    void testSessionBuilder() {
        Session builtSession = Session.builder()
                .id(1L)
                .name("Test Session")
                .date(new Date())
                .description("Test description")
                .teacher(teacher)
                .users(List.of(user1))
                .build();

        assertThat(builtSession.getId()).isEqualTo(1L);
        assertThat(builtSession.getName()).isEqualTo("Test Session");
        assertThat(builtSession.getDescription()).isEqualTo("Test description");
        assertThat(builtSession.getTeacher()).isEqualTo(teacher);
        assertThat(builtSession.getUsers()).hasSize(1);
    }

    @Test
    void testNoArgsConstructor() {
        Session emptySession = new Session();
        assertThat(emptySession).isNotNull();
        assertThat(emptySession.getId()).isNull();
        assertThat(emptySession.getName()).isNull();
    }

    @Test
    void testGettersAndSetters() {
        Session testSession = new Session();
        Date testDate = new Date();

        testSession.setId(2L);
        testSession.setName("Test Name");
        testSession.setDate(testDate);
        testSession.setDescription("Test Description");
        testSession.setTeacher(teacher);

        assertThat(testSession.getId()).isEqualTo(2L);
        assertThat(testSession.getName()).isEqualTo("Test Name");
        assertThat(testSession.getDate()).isEqualTo(testDate);
        assertThat(testSession.getDescription()).isEqualTo("Test Description");
        assertThat(testSession.getTeacher()).isEqualTo(teacher);
    }

    @Test
    void testChainedAccessors() {
        Date testDate = new Date();

        Session chainedSession = new Session()
                .setId(3L)
                .setName("Chained Session")
                .setDate(testDate)
                .setDescription("Chained description")
                .setTeacher(teacher);

        assertThat(chainedSession.getId()).isEqualTo(3L);
        assertThat(chainedSession.getName()).isEqualTo("Chained Session");
        assertThat(chainedSession.getDate()).isEqualTo(testDate);
    }

    @Test
    void testEqualsAndHashCode() {
        Session session1 = Session.builder().id(1L).name("Session 1").build();
        Session session2 = Session.builder().id(1L).name("Session 2").build();
        Session session3 = Session.builder().id(2L).name("Session 1").build();

        assertThat(session1).isEqualTo(session2);
        assertThat(session1.hashCode()).isEqualTo(session2.hashCode());
        assertThat(session1).isNotEqualTo(session3);
    }

    @Test
    void testToString() {
        String sessionString = session.toString();

        assertThat(sessionString).contains("Session");
        assertThat(sessionString).contains("id=1");
        assertThat(sessionString).contains("name=Yoga Session");
    }

    @Test
    void testNameNotBlank() {
        session.setName("");
        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("must not be blank");
    }

    @Test
    void testNameNotNull() {
        session.setName(null);
        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("must not be blank");
    }

    @Test
    void testNameMaxSize() {
        session.setName("A".repeat(51));
        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("size must be between");
    }

    @Test
    void testDateNotNull() {
        session.setDate(null);
        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("must not be null");
    }

    @Test
    void testDescriptionNotNull() {
        session.setDescription(null);
        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("must not be null");
    }

    @Test
    void testDescriptionMaxSize() {
        session.setDescription("A".repeat(2501));
        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("size must be between");
    }

    @Test
    void testUsersRelationship() {
        assertThat(session.getUsers()).isNotNull();
        assertThat(session.getUsers()).hasSize(2);
        assertThat(session.getUsers()).contains(user1, user2);

        User newUser = User.builder()
                .id(3L)
                .email("user3@test.com")
                .firstName("User")
                .lastName("Three")
                .password("password789")
                .admin(false)
                .build();

        session.getUsers().add(newUser);
        assertThat(session.getUsers()).hasSize(3);
        assertThat(session.getUsers()).contains(newUser);
    }

    @Test
    void testMultipleValidationErrors() {
        Session invalidSession = Session.builder()
                .name("")
                .date(null)
                .description(null)
                .build();

        Set<ConstraintViolation<Session>> violations = validator.validate(invalidSession);
        assertThat(violations).hasSize(3);
    }

    @Test
    void testTimestampFields() {
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();

        session.setCreatedAt(createdAt);
        session.setUpdatedAt(updatedAt);

        assertThat(session.getCreatedAt()).isEqualTo(createdAt);
        assertThat(session.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(session.getUpdatedAt()).isAfter(session.getCreatedAt());
    }
}