package ru.podol.events.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import ru.podol.events.kafkaEvent.notifications.EventNotification;
import ru.podol.events.model.notification.NotificationType;
import ru.podol.events.producer.notifications.KafkaEventNotificationsProducer;
import ru.podol.events.service.NotificationService;

import java.time.Duration;
import java.util.*;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        topics = {"notification"},
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092",
                "port=9092"
        }
)
public class KafkaIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private KafkaEventNotificationsProducer kafkaProducer;

    @Autowired
    private NotificationService notificationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testEventNotification_isProcessedViaKafka() throws Exception {
        String topic = "notification";
        Long userId = 1L;
        String title = "Новое событие";
        NotificationType type = NotificationType.INVITE;
        String message = "Вы были приглашены!";

        EventNotification eventNotification = new EventNotification(userId, title, type, message);

        kafkaProducer.sendMessage(eventNotification);

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), new StringDeserializer());
        Consumer<String, String> consumer = consumerFactory.createConsumer();
        consumer.subscribe(Collections.singletonList(topic));

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
        assertThat(records).isNotEmpty();

        Optional<ConsumerRecord<String, String>> found = StreamSupport.stream(records.spliterator(), false)
                .filter(record -> {
                    try {
                        EventNotification received = objectMapper.readValue(record.value(), EventNotification.class);
                        return received.userId().equals(userId);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .findFirst();

        assertThat(found).isPresent();

        consumer.close();
    }
}
