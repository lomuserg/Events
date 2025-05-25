package ru.podol.events.config.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${spring.kafka.topic.user-invited.name}")
    private String userInvitedTopic;

    @Value("${spring.kafka.topic.event-reminder.name}")
    private String eventReminderTopic;

    @Value("${spring.kafka.topic.event-deleted.name}")
    private String eventDeletedTopic;


    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic UserInvitedNotificationTopic() {
        return new NewTopic(userInvitedTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic EventReminderNotificationTopic() {
        return new NewTopic(eventReminderTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic EventDeletedNotificationTopic() {
        return new NewTopic(eventDeletedTopic, 1, (short) 1);
    }
}
