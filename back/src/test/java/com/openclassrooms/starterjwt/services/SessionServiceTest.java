package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import lombok.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private User user;
    private User anotherUser;
    private List<Session> sessionList;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1L)
                .email("john.doe@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("A complex password")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        anotherUser = User.builder()
                .id(2L)
                .email("lorem.ipsum@example.com")
                .lastName("Ipsum")
                .firstName("Lorem")
                .password("A complex password")
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .description("A relaxing yoga session")
                .users(new ArrayList<>())
                .build();

        Session anotherSession = Session.builder()
                .id(2L)
                .name("Pilates Session")
                .description("An energizing pilates session")
                .users(new ArrayList<>())
                .build();

        sessionList = Arrays.asList(session, anotherSession);
    }

    @Test
    public void testCreate() {
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        Session result = sessionService.create(session);

        assertNotNull(result);
        assertEquals(session, result);
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    public void testDelete() {

        sessionService.delete(1L);

        verify(sessionRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindAll() {

        when(sessionRepository.findAll()).thenReturn(sessionList);


        List<Session> result = sessionService.findAll();


        assertNotNull(result);
        assertEquals(sessionList, result);
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    public void testGetById_Found() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        Session result = sessionService.getById(1L);

        assertNotNull(result);
        assertEquals(session.getId(), result.getId());
        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetById_NotFound() {
        when(sessionRepository.findById(99L)).thenReturn(Optional.empty());

        Session result = sessionService.getById(99L);

        assertNull(result);
        verify(sessionRepository, times(1)).findById(99L);
    }

    @Test
    public void testUpdate() {

        Session updatedSession = Session.builder()
                .name("Updated Yoga Session")
                .description("An updated yoga session description")
                .build();

        Session updatedSession1 = Session.builder()
                .id(1L)
                .name("Updated Yoga Session")
                .description("An updated yoga session description")
                .build();


        when(sessionRepository.save(any(Session.class))).thenReturn(updatedSession1);
        Session result = sessionService.update(1L, updatedSession);


        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Updated Yoga Session", result.getName());
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    public void testParticipate_Success() {

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        sessionService.participate(1L, 1L);

        verify(sessionRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(sessionRepository, times(1)).save(session);
        assertTrue(session.getUsers().contains(user));
    }

    @Test
    public void testNoLongerParticipate_Success() {

        session.getUsers().add(user);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);


        sessionService.noLongerParticipate(1L, 1L);

        verify(sessionRepository, times(1)).findById(1L);
        verify(sessionRepository, times(1)).save(session);
        assertFalse(session.getUsers().contains(user));
    }


}