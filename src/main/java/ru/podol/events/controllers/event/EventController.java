package ru.podol.events.controllers.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.podol.events.dtos.UserDto;
import ru.podol.events.dtos.event.EventDto;
import ru.podol.events.services.EventService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/main/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<?> createEvent(@AuthenticationPrincipal UserDto userDto,
                                         @RequestBody @Valid EventDto eventDto) {
        if (isUserDtoIsNull(userDto)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        EventDto createdEvent = eventService.createEvent(userDto.getId(), eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<?> updateEventById(@AuthenticationPrincipal UserDto userDto,
                                                       @RequestBody @Valid EventDto eventDto,
                                                       @PathVariable long eventId) {
        if (isUserDtoIsNull(userDto)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        EventDto event = eventService.updateEvent(eventDto, eventId);

        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> deleteEventById(@AuthenticationPrincipal UserDto userDto,
                                                       @PathVariable long eventId) {
        if (isUserDtoIsNull(userDto)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        eventService.deleteEvent(eventId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getEventsByUser(@AuthenticationPrincipal UserDto userDto) {
        if (isUserDtoIsNull(userDto)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }
        List<EventDto> events = eventService.getEventsByUserIdWithRoles(userDto.getId());

        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEventById(@AuthenticationPrincipal UserDto userDto,
                                                       @PathVariable long eventId) {
        if (isUserDtoIsNull(userDto)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }
        EventDto event = eventService.getEventById(eventId);

        return ResponseEntity.ok(event);
    }

    private boolean isUserDtoIsNull(UserDto userDto) {
        return userDto == null;
    }
}
