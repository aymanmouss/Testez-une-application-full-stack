package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;
    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    void setUp() {
        teacher = createTeacher();
        teacherDto = createTeacherDto();
    }

    @Test
    void findById_ValidId_ReturnsTeacher() {
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        ResponseEntity<?> response = teacherController.findById("1");

        assertSuccessResponse(response, teacherDto);
        verify(teacherService).findById(1L);
        verify(teacherMapper).toDto(teacher);
    }

    @Test
    void findById_NonExistingId_ReturnsNotFound() {
        when(teacherService.findById(99L)).thenReturn(null);

        ResponseEntity<?> response = teacherController.findById("99");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(teacherService).findById(99L);
        verifyNoInteractions(teacherMapper);
    }

    @Test
    void findById_InvalidId_ReturnsBadRequest() {
        ResponseEntity<?> response = teacherController.findById("invalid");

        assertBadRequest(response);
        verifyNoInteractions(teacherService, teacherMapper);
    }

    @Test
    void findAll_ReturnsAllTeachers() {
        List<Teacher> teachers = Arrays.asList(teacher);
        List<TeacherDto> teacherDtos = Arrays.asList(teacherDto);

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        ResponseEntity<?> response = teacherController.findAll();

        assertSuccessResponse(response, teacherDtos);
        verify(teacherService).findAll();
        verify(teacherMapper).toDto(teachers);
    }

    // Helper methods
    private Teacher createTeacher() {
        LocalDateTime now = LocalDateTime.now();
        return Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    private TeacherDto createTeacherDto() {
        LocalDateTime now = LocalDateTime.now();
        return TeacherDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    private void assertSuccessResponse(ResponseEntity<?> response, Object expectedBody) {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    private void assertBadRequest(ResponseEntity<?> response) {
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}