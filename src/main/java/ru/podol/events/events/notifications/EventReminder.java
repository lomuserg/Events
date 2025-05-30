package ru.podol.events.events.notifications;

import java.time.LocalDateTime;
import java.util.List;

public record EventReminder(Long eventId, String eventTitle,List<Long> userIds,
                            LocalDateTime eventTime ) {
}
