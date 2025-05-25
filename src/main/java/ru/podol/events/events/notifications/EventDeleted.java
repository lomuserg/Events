package ru.podol.events.events.notifications;

import java.util.List;

public record EventDeleted(Long eventId, String eventTitle,
                           List<Long> affectedUserIds) {
}
