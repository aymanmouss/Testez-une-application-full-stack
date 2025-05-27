package com.openclassrooms.starterjwt.security.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User user;
    private final String EMAIL = "john.doe@example.com";

    @BeforeEach
    void setUp() {
        user = createUser();
    }

    @Test
    void loadUserByUsername_existingUser_shouldReturnUserDetails() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(EMAIL);

        assertUserDetails(userDetails, user);
        verify(userRepository).findByEmail(EMAIL);
    }

    @Test
    void loadUserByUsername_nonExistingUser_shouldThrowException() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(EMAIL))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User Not Found with email: " + EMAIL);
    }

    private User createUser() {
        LocalDateTime now = LocalDateTime.now();
        return new User(1L, EMAIL, "Doe", "John", "A simple password", false, now, now);
    }

    private void assertUserDetails(UserDetailsImpl userDetails, User expectedUser) {
        assertThat(userDetails.getUsername()).isEqualTo(expectedUser.getEmail());
        assertThat(userDetails.getFirstName()).isEqualTo(expectedUser.getFirstName());
        assertThat(userDetails.getLastName()).isEqualTo(expectedUser.getLastName());
        assertThat(userDetails.getPassword()).isEqualTo(expectedUser.getPassword());
    }
}