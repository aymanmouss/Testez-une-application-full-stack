package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

    @Mock
    private SessionService sessionService;
    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    private Session session;
    private SessionDto sessionDto;

    @BeforeEach
    void setUp() {
        Teacher teacher = createTeacher();
        session = createSession(teacher);
        sessionDto = createSessionDto();
    }

    @Test
    void findById_ValidId_ReturnsSession() {
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.findById("1");

        assertSuccessResponse(response, sessionDto);
        verify(sessionService).getById(1L);
        verify(sessionMapper).toDto(session);
    }

    @Test
    void findById_NonExistingId_ReturnsNotFound() {
        when(sessionService.getById(99L)).thenReturn(null);

        ResponseEntity<?> response = sessionController.findById("99");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(sessionService).getById(99L);
        verifyNoInteractions(sessionMapper);
    }

    @Test
    void findById_InvalidId_ReturnsBadRequest() {
        ResponseEntity<?> response = sessionController.findById("invalid");

        assertBadRequest(response);
        verifyNoInteractions(sessionService, sessionMapper);
    }

    @Test
    void findAll_ReturnsAllSessions() {
        List<Session> sessions = Collections.singletonList(session);
        List<SessionDto> sessionDtos = Collections.singletonList(sessionDto);

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        ResponseEntity<?> response = sessionController.findAll();

        assertSuccessResponse(response, sessionDtos);
        verify(sessionService).findAll();
        verify(sessionMapper).toDto(sessions);
    }

    @Test
    void create_ValidSession_ReturnsCreatedSession() {
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.create(sessionDto);

        assertSuccessResponse(response, sessionDto);
        verify(sessionMapper).toEntity(sessionDto);
        verify(sessionService).create(session);
        verify(sessionMapper).toDto(session);
    }

    @Test
    void update_ValidIdAndSession_ReturnsUpdatedSession() {
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(1L, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.update("1", sessionDto);

        assertSuccessResponse(response, sessionDto);
        verify(sessionMapper).toEntity(sessionDto);
        verify(sessionService).update(1L, session);
        verify(sessionMapper).toDto(session);
    }

    @Test
    void update_InvalidId_ReturnsBadRequest() {
        ResponseEntity<?> response = sessionController.update("invalid", sessionDto);

        assertBadRequest(response);
        verifyNoInteractions(sessionService);
    }

    @Test
    void delete_ValidId_ReturnsOk() {
        when(sessionService.getById(1L)).thenReturn(session);

        ResponseEntity<?> response = sessionController.save("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService).getById(1L);
        verify(sessionService).delete(1L);
    }

    @Test
    void delete_NonExistingId_ReturnsNotFound() {
        when(sessionService.getById(99L)).thenReturn(null);

        ResponseEntity<?> response = sessionController.save("99");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(sessionService).getById(99L);
        verify(sessionService, never()).delete(anyLong());
    }

    @Test
    void delete_InvalidId_ReturnsBadRequest() {
        ResponseEntity<?> response = sessionController.save("invalid");

        assertBadRequest(response);
        verifyNoInteractions(sessionService);
    }

    @Test
    void participate_ValidIds_ReturnsOk() {
        ResponseEntity<?> response = sessionController.participate("1", "2");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService).participate(1L, 2L);
    }

    @Test
    void participate_InvalidId_ReturnsBadRequest() {
        ResponseEntity<?> response = sessionController.participate("invalid", "2");

        assertBadRequest(response);
        verifyNoInteractions(sessionService);
    }

    @Test
    void noLongerParticipate_ValidIds_ReturnsOk() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "2");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService).noLongerParticipate(1L, 2L);
    }

    @Test
    void noLongerParticipate_InvalidId_ReturnsBadRequest() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("invalid", "2");

        assertBadRequest(response);
        verifyNoInteractions(sessionService);
    }

    // Helper methods
    private Teacher createTeacher() {
        return Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    private Session createSession(Teacher teacher) {
        LocalDateTime now = LocalDateTime.now();
        return Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(new Date())
                .description("A yoga session for beginners")
                .teacher(teacher)
                .users(new ArrayList<>())
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    private SessionDto createSessionDto() {
        SessionDto dto = new SessionDto();
        dto.setId(1L);
        dto.setName("Yoga Session");
        dto.setDate(new Date());
        dto.setDescription("A yoga session for beginners");
        dto.setTeacher_id(1L);
        return dto;
    }

    private void assertSuccessResponse(ResponseEntity<?> response, Object expectedBody) {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    private void assertBadRequest(ResponseEntity<?> response) {
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}