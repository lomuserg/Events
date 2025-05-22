package ru.podol.events.controllers.participants;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.podol.events.dtos.UserDto;
import ru.podol.events.dtos.event.EventDto;
import ru.podol.events.dtos.participant.ParticipantDto;
import ru.podol.events.services.ParticipantsService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main/participants")
public class ParticipantsController {
    private final ParticipantsService participantsService;

    @PostMapping()
    public ResponseEntity<?> addParticipantToEvent(@AuthenticationPrincipal UserDto userDto,
                                                   @RequestBody @Valid ParticipantDto participantDto) {
        if (isUserDtoIsNull(userDto)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        ParticipantDto addedParticipantDto = participantsService.addParticipantToEvent(participantDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedParticipantDto);
    }

    @DeleteMapping("/{eventId}/{login}")
    public ResponseEntity<?> deleteParticipantFromEvent(@PathVariable Long eventId,
                                                        @PathVariable String login) {
        participantsService.deleteParticipantFromEvent(eventId, login);
        return ResponseEntity.noContent().build();
    }

    private boolean isUserDtoIsNull(UserDto userDto) {
        return userDto == null;
    }
}
