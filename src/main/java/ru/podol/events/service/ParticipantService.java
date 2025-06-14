package ru.podol.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.podol.events.dto.participant.ParticipantDto;
import ru.podol.events.kafkaEvent.notifications.EventNotification;
import ru.podol.events.mappers.participant.ParticipantMapper;
import ru.podol.events.model.User;
import ru.podol.events.model.UserEventRole;
import ru.podol.events.model.event.Event;
import ru.podol.events.model.notification.NotificationType;
import ru.podol.events.model.participant.Participant;
import ru.podol.events.producer.notifications.KafkaEventNotificationsProducer;
import ru.podol.events.repository.ParticipantRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParticipantService {
    @Lazy
    @Autowired
    private EventService eventService;

    private final UserService userService;
    private final ParticipantMapper participantMapper;
    private final ParticipantRepository participantRepository;
    private final KafkaEventNotificationsProducer kafkaEventNotificationsProducer;

    @Transactional
    public ParticipantDto addParticipantToEvent(ParticipantDto participantDto) {
        User user = userService.findByLogin(participantDto.getLogin());
        Event event = eventService.findById(participantDto.getEventId());
        log.info("Adding participant to event {}", event);
        Participant participant = participantMapper.toParticipant(participantDto);

        participant.setRole(UserEventRole.PARTICIPANT);
        participant.setUser(user);
        participant.setEvent(event);

        log.info("sending notify: adding participant to event {}", event);
        EventNotification userInvited = new EventNotification(
                user.getId(),
                "Вас добавили на мероприятие",
                NotificationType.INVITE,
                event.getTitle()
        );
        kafkaEventNotificationsProducer.sendMessage(userInvited);

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

    public Participant findParticipantByUserIdAndEventId(Long userId, Long eventId) {
        return participantRepository.findByEventIdAndUserId(eventId, userId);
    }

    public Participant findById(Long id) {
        return participantRepository.findById(id);
    }
}
