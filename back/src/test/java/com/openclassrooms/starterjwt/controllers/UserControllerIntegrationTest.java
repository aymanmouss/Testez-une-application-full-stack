package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void findById_ValidId_ReturnsUser() throws Exception {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .lastName("Test")
                .firstName("User")
                .password("password")
                .admin(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(userService.findById(1L)).thenReturn(user);

        // Act & Assert
        mockMvc.perform(get("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.lastName").value("Test"))
                .andExpect(jsonPath("$.firstName").value("User"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void findById_NonExistingId_ReturnsNotFound() throws Exception {
        // Arrange
        when(userService.findById(99L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/user/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void findById_InvalidId_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/user/invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser("test@test.com")
    void delete_ValidIdAndAuthorizedUser_ReturnsOk() throws Exception {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .lastName("Test")
                .firstName("User")
                .password("password")
                .admin(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(userService.findById(1L)).thenReturn(user);

        // Act & Assert
        mockMvc.perform(delete("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).delete(1L);
    }

    @Test
    @WithMockUser(username = "different@test.com", roles = {"USER"})
    void delete_ValidIdButUnauthorizedUser_ReturnsUnauthorized() throws Exception {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .id(1L)
                .email("test@test.com") // Different from the authenticated user
                .lastName("Test")
                .firstName("User")
                .password("password")
                .admin(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(userService.findById(1L)).thenReturn(user);

        // Act & Assert
        mockMvc.perform(delete("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verify(userService, never()).delete(anyLong());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void delete_NonExistingId_ReturnsNotFound() throws Exception {
        // Arrange
        when(userService.findById(99L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(delete("/api/user/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(userService, never()).delete(anyLong());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void delete_InvalidId_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/user/invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(userService, never()).delete(anyLong());
    }
}