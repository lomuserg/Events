package ru.podol.events.controller.participants;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.podol.events.dto.UserDto;
import ru.podol.events.dto.participant.ParticipantDto;
import ru.podol.events.service.ParticipantService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main/participants")
public class ParticipantsController {
    private final ParticipantService participantService;

    @PostMapping()
    public ResponseEntity<?> addParticipantToEvent(@AuthenticationPrincipal UserDto userDto,
                                                   @RequestBody @Valid ParticipantDto participantDto) {
        if (isUserDtoIsNull(userDto)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        ParticipantDto addedParticipantDto = participantService.addParticipantToEvent(participantDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedParticipantDto);
    }

    @DeleteMapping("/{eventId}/{login}")
    public ResponseEntity<?> deleteParticipantFromEvent(@PathVariable Long eventId,
                                                        @PathVariable String login) {
        participantService.deleteParticipantFromEvent(eventId, login);
        return ResponseEntity.noContent().build();
    }

    private boolean isUserDtoIsNull(UserDto userDto) {
        return userDto == null;
    }
}
