package ru.podol.events.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ru.podol.events.dtos.event.EventDto;
import ru.podol.events.mappers.event.EventMapper;
import ru.podol.events.model.User;
import ru.podol.events.model.UserEventRole;
import ru.podol.events.model.event.Event;
import ru.podol.events.model.participant.Participant;
import ru.podol.events.repository.EventRepository;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private UserService userService;

    @Mock
    private ParticipantsService participantsService;

    private final Long userId = 1L;
    private final Long eventId = 100L;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        Field field = EventService.class.getDeclaredField("participantsService");
        field.setAccessible(true);
        field.set(eventService, participantsService);
    }

    @Test
    void testCreateEvent_success() {
        EventDto dto = new EventDto();
        Event event = new Event();
        Event savedEvent = new Event();
        savedEvent.setTitle("Test Event");

        User user = new User();
        user.setLogin("user1");

        when(userService.getUserById(userId)).thenReturn(user);
        when(eventMapper.toEvent(dto)).thenReturn(event);
        when(eventRepository.save(event)).thenReturn(savedEvent);
        when(eventRepository.save(savedEvent)).thenReturn(savedEvent);
        when(eventMapper.toEventDto(savedEvent)).thenReturn(dto);

        EventDto result = eventService.createEvent(userId, dto);

        assertEquals(dto, result);
        verify(eventRepository, times(2)).save(any(Event.class));
    }


    @Test
    void testUpdateEvent_success() {
        EventDto dto = new EventDto();
        Event event = new Event();
        Event updatedEvent = new Event();

        when(eventRepository.findById(eventId)).thenReturn(event);
        doNothing().when(eventMapper).updateEventFromDto(dto, event);
        when(eventRepository.update(event)).thenReturn(updatedEvent);
        when(eventMapper.toEventDto(updatedEvent)).thenReturn(dto);

        EventDto result = eventService.updateEvent(dto, eventId);

        assertEquals(dto, result);
        verify(eventRepository).update(event);
    }

    @Test
    void testDeleteEvent_success() {
        Event event = new Event();
        event.setId(eventId);
        when(eventRepository.findById(eventId)).thenReturn(event);

        eventService.deleteEvent(eventId);

        verify(eventRepository).delete(event);
    }

    @Test
    void testGetEventById_success() {
        Event event = new Event();
        EventDto dto = new EventDto();
        when(eventRepository.findById(userId)).thenReturn(event);
        when(eventMapper.toEventDto(event)).thenReturn(dto);

        EventDto result = eventService.getEventById(userId);

        assertEquals(dto, result);
    }

    @Test
    void testGetEventByIdAndUserId_success() {
        Event event = new Event();
        Participant participant = new Participant();
        EventDto dto = new EventDto();

        when(eventRepository.findById(userId)).thenReturn(event);
        when(participantsService.findParticipantByUserIdAndEventId(eventId, userId)).thenReturn(participant);
        when(eventMapper.toEventDto(event)).thenReturn(dto);

        EventDto result = eventService.getEventByIdAndUserId(userId, eventId);

        assertEquals(dto, result);
        assertEquals(1, event.getParticipants().size());
    }

    @Test
    void testGetEventsByUserIdWithRoles_success() {
        Event event = new Event();
        EventDto dto = new EventDto();
        UserEventRole role = UserEventRole.PARTICIPANT;

        when(eventRepository.findEventsWithRolesByUserId(userId))
                .thenReturn(List.<Object[]>of(new Object[]{event, role}));

        when(eventMapper.toEventDto(event)).thenReturn(dto);

        List<EventDto> result = eventService.getEventsByUserIdWithRoles(userId);

        assertEquals(1, result.size());
        assertEquals(role, result.get(0).getUserEventRole());
    }


    @Test
    void testGetEventsByUserId_success() {
        Event event = new Event();
        EventDto dto = new EventDto();

        when(eventRepository.findByUserId(userId)).thenReturn(List.of(event));
        when(eventMapper.toEventDtos(List.of(event))).thenReturn(List.of(dto));

        List<EventDto> result = eventService.getEventsByUserId(userId);

        assertEquals(1, result.size());
    }
}
