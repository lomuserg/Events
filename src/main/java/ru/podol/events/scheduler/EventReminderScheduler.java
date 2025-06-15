package ru.podol.events.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.podol.events.service.NotificationService;
import ru.podol.events.service.ParticipantService;

@Component
@RequiredArgsConstructor
public class EventReminderScheduler {
    public final ParticipantService participantService;

    @Scheduled(cron = "${spring.notification.remainder.cron}")
    public void remindToEvent() {
        participantService.send24HourReminders();
    }
}
