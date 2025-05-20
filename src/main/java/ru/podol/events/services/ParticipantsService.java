package ru.podol.events.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.podol.events.dtos.UserDto;
import ru.podol.events.dtos.participant.ParticipantDto;
import ru.podol.events.model.UserEventRole;
import ru.podol.events.repository.ParticipantRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParticipantsService {
    private final UserService userService;
    private final EventService eventService;
    private final ParticipantRepository participantRepository;

    public void addParticipantToEvent(ParticipantDto participantDto) {
       UserDto userDto = userService.findByLogin(participantDto.getLogin());
       participantDto.setUserId(userDto.getId());
       participantDto.setRole(UserEventRole.PARTICIPANT);

       System.out.println(">>>>>>>good???? - " + participantDto);
    }
}
