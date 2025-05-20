package ru.podol.events.controllers.participants;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

        participantsService.addParticipantToEvent(participantDto);
        return ResponseEntity.noContent().build();
    }

    private boolean isUserDtoIsNull(UserDto userDto) {
        return userDto == null;
    }
}
