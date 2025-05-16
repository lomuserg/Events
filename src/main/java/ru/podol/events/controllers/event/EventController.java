package ru.podol.events.controllers.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.podol.events.dtos.UserDto;
import ru.podol.events.dtos.event.EventDto;
import ru.podol.events.model.event.Event;
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

        Event event = eventService.createEvent(userDto.getId(), eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getUserEvents(@AuthenticationPrincipal UserDto userDto) {
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }

        List<EventDto> events = eventService.getEventsByUserId(userDto.getId());
        return ResponseEntity.ok(events);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<EventDto> getEventById(@PathVariable Long id,
//                                                 @AuthenticationPrincipal UserDto userDto) {
//        if (userDto == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }
//
//        return ResponseEntity.ok(eventService.getEventById(id, userDto.getId()));
//    }
//
//    @GetMapping("/{id}/is-organizer")
//    public ResponseEntity<Boolean> isUserOrganizer(@PathVariable Long id,
//                                                   @AuthenticationPrincipal UserDto userDto) {
//        if (userDto == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
//        }
//
//        boolean isOrganizer = eventService.isUserOrganizer(id, userDto.getId());
//        return ResponseEntity.ok(isOrganizer);
//    }
}
