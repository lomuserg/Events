package ru.podol.events.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.podol.events.service.NotificationService;

@Component
@RequiredArgsConstructor
public class EventReminderScheduler {
    public final NotificationService notificationService;

    @Scheduled(cron = "${spring.url.hash.cleaner.cron}")
    public void remindToEvent() {
        notificationService.eventRemind();
    }
}
