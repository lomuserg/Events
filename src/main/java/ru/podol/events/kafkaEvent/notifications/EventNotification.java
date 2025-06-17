package ru.podol.events.kafkaEvent.notifications;

import ru.podol.events.model.notification.NotificationType;

public record EventNotification(Long userId,
                                String title,
                                NotificationType type,
                                String message) {}
