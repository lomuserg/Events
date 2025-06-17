package ru.podol.events.kafkaEvent.notifications;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.podol.events.model.notification.NotificationType;

public record EventNotification(@JsonProperty("userId") Long userId,
                                @JsonProperty("title") String title,
                                @JsonProperty("type") NotificationType type,
                                @JsonProperty("message") String message) {}
