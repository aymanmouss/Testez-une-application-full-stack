package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SessionMapperTest {

    @Autowired
    private SessionMapperImpl sessionMapper;

    @Test
    void sessionDtoToSession(){
        Teacher teacher = new Teacher();
        teacher.setId(3L);

        User user = new User();
        user.setId(1L);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setDescription("description");
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setUsers(Collections.singletonList(user.getId()));

        Session session = sessionMapper.toEntity(sessionDto);

        assertNotNull(session);
        assertEquals(sessionDto.getId(),session.getId());
    }

    @Test
    void sessionToSessionDto(){
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        User user = new User();
        user.setId(1L);

        Session session = new Session();
        session.setId(1L);
        session.setDescription("description");
        session.setTeacher(teacher);
        session.setUsers(List.of(user));

        SessionDto sessionDto = sessionMapper.toDto(session);

        assertNotNull(sessionDto);
        assertEquals(session.getId(), sessionDto.getId());
    }
}
