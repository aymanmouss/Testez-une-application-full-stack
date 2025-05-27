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

class TeacherTest {

    private Validator validator;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    @Test
    void testValidTeacher() {
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertThat(violations).isEmpty();
    }

    @Test
    void testTeacherBuilder() {
        Teacher builtTeacher = Teacher.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Smith")
                .build();

        assertThat(builtTeacher.getId()).isEqualTo(1L);
        assertThat(builtTeacher.getFirstName()).isEqualTo("Jane");
        assertThat(builtTeacher.getLastName()).isEqualTo("Smith");
    }

    @Test
    void testNoArgsConstructor() {
        Teacher emptyTeacher = new Teacher();
        assertThat(emptyTeacher).isNotNull();
        assertThat(emptyTeacher.getId()).isNull();
        assertThat(emptyTeacher.getFirstName()).isNull();
        assertThat(emptyTeacher.getLastName()).isNull();
    }

    @Test
    void testGettersAndSetters() {
        Teacher testTeacher = new Teacher();

        testTeacher.setId(2L);
        testTeacher.setFirstName("Alice");
        testTeacher.setLastName("Johnson");

        assertThat(testTeacher.getId()).isEqualTo(2L);
        assertThat(testTeacher.getFirstName()).isEqualTo("Alice");
        assertThat(testTeacher.getLastName()).isEqualTo("Johnson");
    }

    @Test
    void testChainedAccessors() {
        Teacher chainedTeacher = new Teacher()
                .setId(3L)
                .setFirstName("Bob")
                .setLastName("Wilson");

        assertThat(chainedTeacher.getId()).isEqualTo(3L);
        assertThat(chainedTeacher.getFirstName()).isEqualTo("Bob");
        assertThat(chainedTeacher.getLastName()).isEqualTo("Wilson");
    }

    @Test
    void testEqualsAndHashCode() {
        Teacher teacher1 = Teacher.builder().id(1L).firstName("John").lastName("Doe").build();
        Teacher teacher2 = Teacher.builder().id(1L).firstName("Jane").lastName("Smith").build();
        Teacher teacher3 = Teacher.builder().id(2L).firstName("John").lastName("Doe").build();

        assertThat(teacher1).isEqualTo(teacher2);
        assertThat(teacher1.hashCode()).isEqualTo(teacher2.hashCode());
        assertThat(teacher1).isNotEqualTo(teacher3);
    }

    @Test
    void testToString() {
        String teacherString = teacher.toString();

        assertThat(teacherString).contains("Teacher");
        assertThat(teacherString).contains("id=1");
        assertThat(teacherString).contains("firstName=John");
        assertThat(teacherString).contains("lastName=Doe");
    }

    @Test
    void testFirstNameNotBlank() {
        teacher.setFirstName("");
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("must not be blank");
    }

    @Test
    void testFirstNameNotNull() {
        teacher.setFirstName(null);
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("must not be blank");
    }

    @Test
    void testFirstNameMaxSize() {
        teacher.setFirstName("A".repeat(21));
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("size must be between");
    }

    @Test
    void testFirstNameValidMaxSize() {
        teacher.setFirstName("A".repeat(20));
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        assertThat(violations).isEmpty();
    }

    @Test
    void testLastNameNotBlank() {
        teacher.setLastName("");
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("must not be blank");
    }

    @Test
    void testLastNameNotNull() {
        teacher.setLastName(null);
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("must not be blank");
    }

    @Test
    void testLastNameMaxSize() {
        teacher.setLastName("A".repeat(21));
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("size must be between");
    }

    @Test
    void testLastNameValidMaxSize() {
        teacher.setLastName("A".repeat(20));
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        assertThat(violations).isEmpty();
    }

    @Test
    void testMultipleValidationErrors() {
        Teacher invalidTeacher = Teacher.builder()
                .firstName("")
                .lastName(null)
                .build();

        Set<ConstraintViolation<Teacher>> violations = validator.validate(invalidTeacher);
        assertThat(violations).hasSize(2);
    }

    @Test
    void testTimestampFields() {
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();

        teacher.setCreatedAt(createdAt);
        teacher.setUpdatedAt(updatedAt);

        assertThat(teacher.getCreatedAt()).isEqualTo(createdAt);
        assertThat(teacher.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(teacher.getUpdatedAt()).isAfter(teacher.getCreatedAt());
    }
}
