package ru.podol.events.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ru.podol.events.dtos.participant.ParticipantDto;
import ru.podol.events.mappers.participant.ParticipantMapper;
import ru.podol.events.model.User;
import ru.podol.events.model.UserEventRole;
import ru.podol.events.model.event.Event;
import ru.podol.events.model.participant.Participant;
import ru.podol.events.repository.ParticipantRepository;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParticipantsServiceTest {

    @InjectMocks
    private ParticipantsService participantsService;

    @Mock
    private UserService userService;

    @Mock
    private ParticipantMapper participantMapper;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private EventService eventService;

    private final String login = "user1";
    private final Long eventId = 100L;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        Field field = ParticipantsService.class.getDeclaredField("eventService");
        field.setAccessible(true);
        field.set(participantsService, eventService);
    }

    @Test
    void testAddParticipantToEvent_success() {
        ParticipantDto dto = new ParticipantDto();
        dto.setLogin(login);
        dto.setEventId(eventId);

        User user = new User();
        user.setLogin(login);

        Event event = new Event();
        event.setId(eventId);

        Participant participant = new Participant();
        Participant savedParticipant = new Participant();

        ParticipantDto expectedDto = new ParticipantDto();

        when(userService.findByLogin(login)).thenReturn(user);
        when(eventService.findById(eventId)).thenReturn(event);
        when(participantMapper.toParticipant(dto)).thenReturn(participant);
        when(participantRepository.save(participant)).thenReturn(savedParticipant);
        when(participantMapper.toParticipantDto(savedParticipant)).thenReturn(expectedDto);

        ParticipantDto result = participantsService.addParticipantToEvent(dto);

        assertEquals(expectedDto, result);
        assertEquals(UserEventRole.PARTICIPANT, participant.getRole());
        assertEquals(user, participant.getUser());
        assertEquals(event, participant.getEvent());

        verify(participantRepository).save(participant);
    }

    @Test
    void testDeleteParticipantFromEvent_success() {
        Participant participant = new Participant();
        when(participantRepository.findByEventIdAndUserLogin(eventId, login)).thenReturn(participant);

        participantsService.deleteParticipantFromEvent(eventId, login);

        verify(participantRepository).delete(participant);
    }

    @Test
    void testFindParticipantByUserIdAndEventId_success() {
        Long userId = 10L;
        Participant participant = new Participant();

        when(participantRepository.findByEventIdAndUserId(eventId, userId)).thenReturn(participant);

        Participant result = participantsService.findParticipantByUserIdAndEventId(userId, eventId);

        assertEquals(participant, result);
    }
}
