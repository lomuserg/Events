package ru.podol.events.kafkaEvent.notifications;

import java.time.LocalDateTime;
import java.util.List;

public record EventReminder(Long eventId, String eventTitle,List<Long> userIds,
                            LocalDateTime eventTime ) {
}
