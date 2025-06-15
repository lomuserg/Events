package ru.podol.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParticipantService {
    @Lazy
    private EventService eventService;
    @Lazy
    private NotificationService notificationService;

    private static final int REMIND_24_HOURS = 24;
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

    public void send24HourReminders() {
        List<Participant> participants = findParticipantsForReminder24Hours();

        participants.forEach(participant -> {
            notificationService.sendReminder(
                    participant.getUser().getId(),
                    "Напоминание о мероприятии",
                    NotificationType.REMIND,
                    String.format("Через %d часов: %s", REMIND_24_HOURS, participant.getEvent().getTitle())
            );
            participant.setReminderSent(true);
        });

        participantRepository.saveAll(participants);
    }

    private List<Participant> findParticipantsForReminder24Hours() {
        LocalDateTime now = LocalDateTime.now();
        return participantRepository.findParticipantsForReminder(
                now,
                now.plusHours(REMIND_24_HOURS)
        );
    }
}
