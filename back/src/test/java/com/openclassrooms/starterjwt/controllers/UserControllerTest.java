package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @BeforeEach
    void setUp() {
        user = createUser();
        userDto = createUserDto();
    }

    @Test
    void findById_ValidId_ReturnsUser() {
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<?> response = userController.findById("1");

        assertSuccessResponse(response, userDto);
        verify(userService).findById(1L);
        verify(userMapper).toDto(user);
    }

    @Test
    void findById_NonExistingId_ReturnsNotFound() {
        when(userService.findById(99L)).thenReturn(null);

        ResponseEntity<?> response = userController.findById("99");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService).findById(99L);
        verifyNoInteractions(userMapper);
    }

    @Test
    void findById_InvalidId_ReturnsBadRequest() {
        ResponseEntity<?> response = userController.findById("invalid");

        assertBadRequest(response);
        verifyNoInteractions(userService, userMapper);
    }

    @Test
    void delete_ValidIdAndAuthorizedUser_ReturnsOk() {
        setupSecurityContext();
        when(userService.findById(1L)).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@test.com");

        ResponseEntity<?> response = userController.save("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).findById(1L);
        verify(userService).delete(1L);
    }

    @Test
    void delete_ValidIdButUnauthorizedUser_ReturnsUnauthorized() {
        setupSecurityContext();
        when(userService.findById(1L)).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("another@test.com");

        ResponseEntity<?> response = userController.save("1");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userService).findById(1L);
        verify(userService, never()).delete(anyLong());
    }

    @Test
    void delete_NonExistingId_ReturnsNotFound() {
        when(userService.findById(99L)).thenReturn(null);

        ResponseEntity<?> response = userController.save("99");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService).findById(99L);
        verify(userService, never()).delete(anyLong());
    }

    @Test
    void delete_InvalidId_ReturnsBadRequest() {
        ResponseEntity<?> response = userController.save("invalid");

        assertBadRequest(response);
        verifyNoInteractions(userService);
    }

    // Helper methods
    private User createUser() {
        LocalDateTime now = LocalDateTime.now();
        return User.builder()
                .id(1L)
                .email("test@test.com")
                .lastName("Test")
                .firstName("User")
                .password("password")
                .admin(false)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    private UserDto createUserDto() {
        LocalDateTime now = LocalDateTime.now();
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setEmail("test@test.com");
        dto.setLastName("Test");
        dto.setFirstName("User");
        dto.setAdmin(false);
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        return dto;
    }

    private void setupSecurityContext() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    private void assertSuccessResponse(ResponseEntity<?> response, Object expectedBody) {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    private void assertBadRequest(ResponseEntity<?> response) {
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}