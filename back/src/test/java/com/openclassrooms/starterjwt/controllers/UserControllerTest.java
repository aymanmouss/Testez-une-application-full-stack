package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private UserController userController;

    private User user;
    private UserDto userDto;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        LocalDateTime now = LocalDateTime.now();

        user = User.builder()
                .id(1L)
                .email("test@test.com")
                .lastName("Test")
                .firstName("User")
                .password("password")
                .admin(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@test.com");
        userDto.setLastName("Test");
        userDto.setFirstName("User");
        userDto.setAdmin(false);
        userDto.setCreatedAt(now);
        userDto.setUpdatedAt(now);

        // Mock SecurityContextHolder
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void findById_ValidId_ReturnsUser() {
        // Arrange
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Act
        ResponseEntity<?> response = userController.findById("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
        verify(userService).findById(1L);
        verify(userMapper).toDto(user);
    }

    @Test
    void findById_NonExistingId_ReturnsNotFound() {
        // Arrange
        when(userService.findById(99L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.findById("99");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService).findById(99L);
        verifyNoInteractions(userMapper);
    }

    @Test
    void findById_InvalidId_ReturnsBadRequest() {
        // Act
        ResponseEntity<?> response = userController.findById("invalid");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(userService);
        verifyNoInteractions(userMapper);
    }

    @Test
    void delete_ValidIdAndAuthorizedUser_ReturnsOk() {
        // Arrange
        when(userService.findById(1L)).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@test.com");

        // Act
        ResponseEntity<?> response = userController.save("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).findById(1L);
        verify(userService).delete(1L);
    }

    @Test
    void delete_ValidIdButUnauthorizedUser_ReturnsUnauthorized() {
        // Arrange
        when(userService.findById(1L)).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("another@test.com");

        // Act
        ResponseEntity<?> response = userController.save("1");

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userService).findById(1L);
        verify(userService, never()).delete(anyLong());
    }

    @Test
    void delete_NonExistingId_ReturnsNotFound() {
        // Arrange
        when(userService.findById(99L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.save("99");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService).findById(99L);
        verify(userService, never()).delete(anyLong());
    }

    @Test
    void delete_InvalidId_ReturnsBadRequest() {
        // Act
        ResponseEntity<?> response = userController.save("invalid");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(userService);
    }
}