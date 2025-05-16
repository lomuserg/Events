package ru.podol.events.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.podol.events.dtos.event.EventDto;
import ru.podol.events.exceptions.AppException;
import ru.podol.events.model.event.Event;
import ru.podol.events.model.User;
import ru.podol.events.model.event.EventUser;
import ru.podol.events.model.UserEventRole;
import ru.podol.events.repository.EventRepository;
import ru.podol.events.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public Event createEvent(Long userId, EventDto dto) {
        User user = getUserById(userId);
        Event event = new Event();
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setEventDateTime(dto.getEventDateTime());
        event.setLocation(dto.getLocation());
        event.setEventCategory(dto.getEventCategory());

        Event savedEvent = eventRepository.save(event);

        // Добавляем создателя как организатора
        EventUser eventUser = new EventUser();
        eventUser.setUser(user);
        eventUser.setEvent(savedEvent);
        eventUser.setRole(UserEventRole.ORGANIZER);
        savedEvent.getParticipants().add(eventUser);

        return eventRepository.save(savedEvent);
    }

    public List<EventDto> getEventsByUserId(Long userId) {
        User user = getUserById(userId);
        return eventRepository.findByUser(user).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public EventDto getEventByIdAndUserId(Long eventId, Long userId) {
        User user = getUserById(userId);
        Event event = eventRepository.findById(eventId);

        if (!event.getParticipants().stream()
                .anyMatch(eu -> eu.getUser().getId().equals(userId))) {
            throw new AppException("You are not part of this event", HttpStatus.FORBIDDEN);
        }

        return convertToDto(event);
    }

    public boolean isUserOrganizer(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId);
        User user = getUserById(userId);
        return eventRepository.isUserOrganizer(event, user);
    }

    private EventDto convertToDto(Event event) {
        return EventDto.builder()
                .title(event.getTitle())
                .description(event.getDescription())
                .eventDateTime(event.getEventDateTime())
                .location(event.getLocation())
                .eventCategory(event.getEventCategory())
                .build();
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
    }
}