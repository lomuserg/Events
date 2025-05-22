package ru.podol.events.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.podol.events.dtos.UserDto;
import ru.podol.events.dtos.participant.ParticipantDto;
import ru.podol.events.mappers.participant.ParticipantMapper;
import ru.podol.events.model.User;
import ru.podol.events.model.UserEventRole;
import ru.podol.events.model.event.Event;
import ru.podol.events.model.participant.Participant;
import ru.podol.events.repository.ParticipantRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParticipantsService {
    private final UserService userService;
    private final EventService eventService;
    private final ParticipantMapper participantMapper;
    private final ParticipantRepository participantRepository;

    @Transactional
    public ParticipantDto addParticipantToEvent(ParticipantDto participantDto) {
       User user = userService.findByLogin(participantDto.getLogin());
       Event event = eventService.findById(participantDto.getEventId());
       log.info("Adding participant to event {}", event);
       Participant participant = participantMapper.toParticipant(participantDto);

       participant.setRole(UserEventRole.PARTICIPANT);
       participant.setUser(user);
       participant.setEvent(event);
       log.info("Participant {} added to event", participantMapper.toParticipantDto(participant));
       return participantMapper.toParticipantDto(participantRepository.save(participant));
    }

    @Transactional
    public void deleteParticipantFromEvent(Long eventId, String login) {
        log.info("Deleting participant {} from event {}", login, eventId);
        Participant participant = participantRepository.findByEventIdAndUserLogin(eventId, login);

        participantRepository.delete(participant);
        log.info("Participant {} deleted from event {}", login, eventId);
    }
}
