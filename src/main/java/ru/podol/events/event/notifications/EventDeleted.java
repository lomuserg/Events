package ru.podol.events.event.notifications;

import java.util.List;

public record EventDeleted(Long eventId, String eventTitle,
                           List<Long> affectedUserIds) {
}
