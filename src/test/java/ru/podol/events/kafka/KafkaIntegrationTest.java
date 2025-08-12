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
import ru.podol.events.dto.auth.SignUpDto;
import ru.podol.events.dto.participant.ParticipantDto;
import ru.podol.events.kafkaEvent.notifications.EventNotification;
import ru.podol.events.model.User;
import ru.podol.events.model.notification.NotificationType;
import ru.podol.events.producer.notifications.KafkaEventNotificationsProducer;
import ru.podol.events.service.NotificationService;
import ru.podol.events.service.ParticipantService;
import ru.podol.events.service.UserService;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
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

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    void tearDown() {
        try {
            Thread.sleep(1000); // Даём Windows время закрыть дескрипторы
        } catch (InterruptedException ignored) {}
    }

    @Test
    void testEventNotification_isProcessedViaKafka() throws Exception {
        String topic = "notification";
        Long userId = 1L;
        String title = "Новое событие";
        NotificationType type = NotificationType.INVITE;
        String message = "Вы были приглашены!";

        EventNotification eventNotification = new EventNotification(userId, title, type, message);

        // Отправляем сообщение через KafkaProducer
        kafkaProducer.sendMessage(eventNotification);

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(
                consumerProps,
                new StringDeserializer(),
                new StringDeserializer()
        );
        Consumer<String, String> consumer = consumerFactory.createConsumer();
        consumer.subscribe(Collections.singletonList(topic));

        // Читаем из топика
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));

        // Проверяем, что записи не пустые
        assertThat(records).isNotEmpty();

        // Ищем нужное сообщение
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

        // Если найдено — выводим сообщение
        if (found.isPresent()) {
            System.out.println("Уведомление обработалось");
        } else {
            System.out.println("Уведомление НЕ обработалось");
        }

        // Тестовая проверка
        assertThat(found).isPresent();
    }

}
