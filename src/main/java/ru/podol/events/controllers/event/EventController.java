package ru.podol.events.controllers.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.podol.events.dtos.UserDto;
import ru.podol.events.dtos.event.EventDto;
import ru.podol.events.model.event.Event;
import ru.podol.events.services.EventService;

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
}
