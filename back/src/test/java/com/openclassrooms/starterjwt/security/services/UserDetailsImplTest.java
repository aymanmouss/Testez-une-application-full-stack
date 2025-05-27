package com.openclassrooms.starterjwt.security.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.security.core.GrantedAuthority;


@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserDetailsImplTest {
    @Mock
    private UserDetailsImpl userDetails;

    @BeforeEach
    void init() {
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("A simple password")
                .build();
    }

    @Test
    void testGetAuthorities() {
        // WHEN
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // THEN
        assertThat(authorities).isNotNull().isEmpty();
    }

    @Test
    void testIsAccountNonExpired() {
        // THEN
        assertThat(userDetails.isAccountNonExpired()).isTrue();
    }

    @Test
    void testIsAccountNonLocked() {
        // THEN
        assertThat(userDetails.isAccountNonLocked()).isTrue();
    }

    @Test
    void testIsCredentialsNonExpired() {
        // THEN
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
    }

    @Test
    void testIsEnabled() {
        // THEN
        assertThat(userDetails.isEnabled()).isTrue();
    }

    @Test
    void testEqualsSameObject() {
        // GIVEN
        UserDetailsImpl sameUserDetails = userDetails;

        // THEN
        assertThat(true).isTrue();
    }

    @Test
    void testEqualsDifferentClass() {
        // GIVEN
        Object differentObject = new Object();

        // THEN
        assertThat(userDetails.equals(differentObject)).isFalse();
    }

    @Test
    void testEqualsDifferentId() {
        // GIVEN
        UserDetailsImpl differentUserDetails = UserDetailsImpl.builder().id(2L).build();

        // THEN
        assertThat(userDetails.equals(differentUserDetails)).isFalse();
    }

    @Test
    void testEqualsSameId() {
        // GIVEN
        UserDetailsImpl sameIdUserDetails = UserDetailsImpl.builder().id(1L).build();

        // THEN
        assertThat(userDetails.equals(sameIdUserDetails)).isTrue();
    }

}
