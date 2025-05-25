package ru.podol.events.producer.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.podol.events.events.notifications.UserInvited;
import ru.podol.events.producer.AbstractProducer;

@Component
public class UserInvitedProducer extends AbstractProducer<UserInvited> {

    public UserInvitedProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper,
            @Value("${spring.kafka.topic.user-invited.name}") String topicName
    ) {
        super(kafkaTemplate, topicName, objectMapper);
    }
}
