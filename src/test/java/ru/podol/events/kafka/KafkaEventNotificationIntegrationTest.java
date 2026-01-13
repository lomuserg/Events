package ru.podol.events.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import ru.podol.events.kafkaEvent.notifications.EventNotification;
import ru.podol.events.model.notification.NotificationType;
import ru.podol.events.producer.notifications.KafkaEventNotificationsProducer;
import ru.podol.events.service.NotificationService;

import java.time.Duration;
import java.util.*;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(
        partitions = 1,
        topics = {"notification"},
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092",
                "port=9092"
        }
)

public class KafkaEventNotificationIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    void contextLoads() {
        assertThat(embeddedKafkaBroker).isNotNull();
    }

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private KafkaEventNotificationsProducer kafkaProducer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Consumer<String, String> consumer;

    @BeforeEach
    void setUp() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("notification-group", "true", embeddedKafkaBroker);
        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(
                consumerProps,
                new StringDeserializer(),
                new StringDeserializer()
        );
        consumer = consumerFactory.createConsumer();
        consumer.subscribe(Collections.singletonList("notification"));
    }

    @AfterEach
    void tearDown() {
        if (consumer != null) {
            consumer.close();
        }
    }

    @Test
    void testNotificationSentAndProcessed() throws Exception {
        Long userId = 1L;
        String title = "Вы были приглашены!";
        String message = "Вас добавили на мероприятие";
        EventNotification eventNotification = new EventNotification(userId, title, NotificationType.INVITE, message);

        // Отправляем событие
        kafkaProducer.sendMessage(eventNotification);

        // Читаем из Kafka
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
        assertThat(records).isNotEmpty();

        Optional<ConsumerRecord<String, String>> received = StreamSupport.stream(records.spliterator(), false)
                .findFirst();

        assertThat(received).isPresent();
        EventNotification parsed = objectMapper.readValue(received.get().value(), EventNotification.class);
        assertThat(parsed.userId()).isEqualTo(userId);

        // Ждём обработки события и сохранения в БД
        await().atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofMillis(500))
                .untilAsserted(() -> {
                    List<?> notifications = notificationService.getNotificationsByUserId(userId);
                    assertThat(notifications).isNotEmpty();
                });
    }
}
