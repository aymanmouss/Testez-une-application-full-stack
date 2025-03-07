package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    private AutoCloseable closeable;
    private Session session;
    private SessionDto sessionDto;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        LocalDateTime now = LocalDateTime.now();
        Date testDate = new Date();

        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(testDate)
                .description("A yoga session for beginners")
                .teacher(teacher)
                .users(new ArrayList<>())
                .createdAt(now)
                .updatedAt(now)
                .build();

        sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Yoga Session");
        sessionDto.setDate(testDate);
        sessionDto.setDescription("A yoga session for beginners");
        sessionDto.setTeacher_id(1L);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void findById_ValidId_ReturnsSession() {
        // Arrange
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Act
        ResponseEntity<?> response = sessionController.findById("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
        verify(sessionService).getById(1L);
        verify(sessionMapper).toDto(session);
    }

    @Test
    void findById_NonExistingId_ReturnsNotFound() {
        // Arrange
        when(sessionService.getById(99L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = sessionController.findById("99");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(sessionService).getById(99L);
        verifyNoInteractions(sessionMapper);
    }

    @Test
    void findById_InvalidId_ReturnsBadRequest() {
        // Act
        ResponseEntity<?> response = sessionController.findById("invalid");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(sessionService);
        verifyNoInteractions(sessionMapper);
    }

    @Test
    void findAll_ReturnsAllSessions() {
        // Arrange
        List<Session> sessions = Arrays.asList(session);
        List<SessionDto> sessionDtos = Arrays.asList(sessionDto);

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        // Act
        ResponseEntity<?> response = sessionController.findAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDtos, response.getBody());
        verify(sessionService).findAll();
        verify(sessionMapper).toDto(sessions);
    }

    @Test
    void create_ValidSession_ReturnsCreatedSession() {
        // Arrange
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Act
        ResponseEntity<?> response = sessionController.create(sessionDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
        verify(sessionMapper).toEntity(sessionDto);
        verify(sessionService).create(session);
        verify(sessionMapper).toDto(session);
    }

    @Test
    void update_ValidIdAndSession_ReturnsUpdatedSession() {
        // Arrange
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(1L, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Act
        ResponseEntity<?> response = sessionController.update("1", sessionDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
        verify(sessionMapper).toEntity(sessionDto);
        verify(sessionService).update(1L, session);
        verify(sessionMapper).toDto(session);
    }

    @Test
    void update_InvalidId_ReturnsBadRequest() {
        // Act
        ResponseEntity<?> response = sessionController.update("invalid", sessionDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(sessionService);
    }

    @Test
    void delete_ValidId_ReturnsOk() {
        // Arrange
        when(sessionService.getById(1L)).thenReturn(session);

        // Act
        ResponseEntity<?> response = sessionController.save("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService).getById(1L);
        verify(sessionService).delete(1L);
    }

    @Test
    void delete_NonExistingId_ReturnsNotFound() {
        // Arrange
        when(sessionService.getById(99L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = sessionController.save("99");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(sessionService).getById(99L);
        verify(sessionService, never()).delete(anyLong());
    }

    @Test
    void delete_InvalidId_ReturnsBadRequest() {
        // Act
        ResponseEntity<?> response = sessionController.save("invalid");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(sessionService);
    }

    @Test
    void participate_ValidIds_ReturnsOk() {
        // Act
        ResponseEntity<?> response = sessionController.participate("1", "2");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService).participate(1L, 2L);
    }

    @Test
    void participate_InvalidId_ReturnsBadRequest() {
        // Act
        ResponseEntity<?> response = sessionController.participate("invalid", "2");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(sessionService);
    }

    @Test
    void noLongerParticipate_ValidIds_ReturnsOk() {
        // Act
        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "2");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService).noLongerParticipate(1L, 2L);
    }

    @Test
    void noLongerParticipate_InvalidId_ReturnsBadRequest() {
        // Act
        ResponseEntity<?> response = sessionController.noLongerParticipate("invalid", "2");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(sessionService);
    }
}
