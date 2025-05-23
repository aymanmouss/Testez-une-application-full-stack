package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    public void setUp() {
        authController = new AuthController(
                authenticationManager,
                passwordEncoder,
                jwtUtils,
                userRepository
        );
    }

    @Test
    @DisplayName("authenticateUser method, return JWT response")
    void whenUserAuthenticated_thenReturnJwtResponse(){
        LoginRequest loginRequest = new LoginRequest();
        String email = "test@example.com";
        String password = "encodedPassword";

        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJzYXJhaCJ9";

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(15L)
                .username(email)
                .lastName("Doe")
                .firstName("John")
                .admin(false)
                .password(password)
                .build();

        User user = new User();
        user.setAdmin(false);
        boolean isAdmin = user.isAdmin();

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password))).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
        securityContext.setAuthentication(authentication);

        when(jwtUtils.generateJwtToken(authentication)).thenReturn(token);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(user));

        JwtResponse jwtResponse = new JwtResponse(
                token,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                isAdmin
        );

        ResponseEntity<?> authenticateUser = authController.authenticateUser(loginRequest);
        ResponseEntity<?> responseEntityOK = ResponseEntity.ok(jwtResponse);

        assertEquals(authenticateUser.getStatusCode(),responseEntityOK.getStatusCode());
        verify(authenticationManager, times(1)).authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        verify(jwtUtils, times(1)).generateJwtToken(authentication);
        verify(userRepository, times(1)).findByEmail(userDetails.getUsername());
    }

    @Test
    @DisplayName("authenticateUser method, bad credentials")
    void whenBadCredentials_thenNoAuthentication(){
        LoginRequest loginRequest = new LoginRequest();
        String email = "test@example.com";
        String password = "encodedPassword";

        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        String token = "invalid_token_attempt";
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .username(email)
                .password(password)
                .build();

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password))).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        securityContext.setAuthentication(authentication);

        when(jwtUtils.generateJwtToken(authentication)).thenReturn(token);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        authController.authenticateUser(loginRequest);

        assertNull(user);
        verify(authenticationManager, times(1)).authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        verify(jwtUtils, times(1)).generateJwtToken(authentication);
        verify(userRepository, times(2)).findByEmail(email);
    }

    @Test
    @DisplayName("registerUser method, return response entity ok")
    void whenValidSignUpRequest_thenReturnResponseEntityOK(){
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("encodedPassword");

        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setPassword(signupRequest.getPassword());
        user.setAdmin(false);

        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn(user.getPassword());
        when(userRepository.save(user)).thenReturn(user);

        MessageResponse messageResponse = new MessageResponse("User registered successfully!");
        ResponseEntity<?> responseEntity = ResponseEntity.ok(messageResponse);
        ResponseEntity<?> registerUser = authController.registerUser(signupRequest);
        assertEquals(registerUser.getStatusCode(), responseEntity.getStatusCode());
        verify(passwordEncoder, times(1)).encode(user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("registerUser method, return bad request")
    void whenEmailAlreadyExists_thenReturnBadRequest(){
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("alex.rodriguez@yogastudio.com");
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        MessageResponse messageResponse = new MessageResponse("Error: Email is already taken!");
        ResponseEntity<?> responseEntityBadRequest = ResponseEntity.badRequest().body(messageResponse);
        ResponseEntity<?> registerUser = authController.registerUser(signupRequest);

        assertEquals(registerUser.getStatusCode(), responseEntityBadRequest.getStatusCode());
        verify(userRepository, times(1)).existsByEmail(signupRequest.getEmail());
    }
}