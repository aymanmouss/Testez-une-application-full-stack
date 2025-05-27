package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SessionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "UserMock")
    void findById_ValidId_ReturnsSession() throws Exception {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Date testDate = new Date();

        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        Session session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(testDate)
                .description("A yoga session for beginners")
                .teacher(teacher)
                .users(new ArrayList<>())
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(sessionService.getById(1L)).thenReturn(session);

        // Act & Assert
        mockMvc.perform(get("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "UserMock")
    void findById_NonExistingId_ReturnsNotFound() throws Exception {
        // Arrange
        when(sessionService.getById(99L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/session/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "UserMock", roles = {"USER"})
    void findById_InvalidId_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/session/invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "UserMock", roles = {"USER"})
    void findAll_ReturnsAllSessions() throws Exception {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Date testDate = new Date();

        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        Session session1 = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(testDate)
                .description("A yoga session for beginners")
                .teacher(teacher)
                .users(new ArrayList<>())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Session session2 = Session.builder()
                .id(2L)
                .name("Meditation Session")
                .date(testDate)
                .description("A meditation session for everyone")
                .teacher(teacher)
                .users(new ArrayList<>())
                .createdAt(now)
                .updatedAt(now)
                .build();

        List<Session> sessions = Arrays.asList(session1, session2);

        when(sessionService.findAll()).thenReturn(sessions);

        // Act & Assert
        mockMvc.perform(get("/api/session")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "UserMock")
    void create_ValidSession_ReturnsCreatedSession() throws Exception {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Date testDate = new Date();

        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        Session session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(testDate)
                .description("A yoga session for beginners")
                .teacher(teacher)
                .users(new ArrayList<>())
                .createdAt(now)
                .updatedAt(now)
                .build();

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga Session");
        sessionDto.setDate(testDate);
        sessionDto.setDescription("A yoga session for beginners");
        sessionDto.setTeacher_id(1L);

        when(sessionService.create(any(Session.class))).thenReturn(session);

        // Act & Assert
        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Yoga Session"));
    }

    @Test
    @WithMockUser(username = "UserMock")
    void update_ValidIdAndSession_ReturnsUpdatedSession() throws Exception {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Date testDate = new Date();

        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        Session updatedSession = Session.builder()
                .id(1L)
                .name("Updated Yoga Session")
                .date(testDate)
                .description("An updated yoga session for beginners")
                .teacher(teacher)
                .users(new ArrayList<>())
                .createdAt(now)
                .updatedAt(now)
                .build();

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated Yoga Session");
        sessionDto.setDate(testDate);
        sessionDto.setDescription("An updated yoga session for beginners");
        sessionDto.setTeacher_id(1L);

        when(sessionService.update(eq(1L), any(Session.class))).thenReturn(updatedSession);

        // Act & Assert
        mockMvc.perform(put("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Yoga Session"));
    }

    @Test
    @WithMockUser(username = "UserMock")
    void delete_ValidId_ReturnsOk() throws Exception {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Date testDate = new Date();

        Session session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(testDate)
                .description("A yoga session for beginners")
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(sessionService.getById(1L)).thenReturn(session);

        // Act & Assert
        mockMvc.perform(delete("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(sessionService).delete(1L);
    }

    @Test
    @WithMockUser(username = "UserMock")
    void participate_ValidIds_ReturnsOk() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/session/1/participate/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(sessionService).participate(1L, 2L);
    }

    @Test
    @WithMockUser(username = "UserMock")
    void noLongerParticipate_ValidIds_ReturnsOk() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/session/1/participate/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(sessionService).noLongerParticipate(1L, 2L);
    }
}