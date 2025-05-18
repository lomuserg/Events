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
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        EventDto createdEvent = eventService.createEvent(userDto.getId(), eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getEventsByUser(@AuthenticationPrincipal UserDto userDto) {
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }
        List<EventDto> events = eventService.getEventsByUserIdWithRoles(userDto.getId());
        System.out.println(events.toString());

        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<List<EventDto>> getEventById(@AuthenticationPrincipal UserDto userDto,
                                                       @PathVariable long eventId) {
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }
        List<EventDto> events = eventService.getEventsByUserId(userDto.getId());
        System.out.println(events.toString());

        return ResponseEntity.ok(events);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<List<EventDto>> addParticipantToEventById(@AuthenticationPrincipal UserDto userDto,
                                                       @RequestBody @Valid EventDto eventDto,
                                                       @PathVariable long eventId) {
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }
        List<EventDto> events = eventService.getEventsByUserId(userDto.getId());
        System.out.println(events.toString());

        return ResponseEntity.ok(events);
    }

}
