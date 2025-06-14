package ru.podol.events.kafkaEvent.notifications;

import java.util.List;

public record EventDeleted(Long eventId, String eventTitle,
                           List<Long> affectedUserIds) {
}
