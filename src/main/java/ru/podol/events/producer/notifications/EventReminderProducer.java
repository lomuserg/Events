package ru.podol.events.producer.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.podol.events.kafkaEvent.notifications.EventReminder;
import ru.podol.events.producer.AbstractProducer;

@Component
public class EventReminderProducer extends AbstractProducer<EventReminder> {

    public EventReminderProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper,
            @Value("${spring.kafka.topic.event-reminder.name}") String topicName
    ) {
        super(kafkaTemplate, topicName, objectMapper);
    }
}
