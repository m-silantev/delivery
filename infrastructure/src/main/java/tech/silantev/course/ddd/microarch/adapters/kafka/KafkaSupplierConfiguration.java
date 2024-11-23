package tech.silantev.course.ddd.microarch.adapters.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static tech.silantev.course.ddd.microarch.adapters.kafka.NotificationServiceAdapter.TOPIC_NAME;

@Configuration
public class KafkaSupplierConfiguration {
    @Bean
    public NewTopic notificationOrderStatusChangedTopic() {
        return TopicBuilder.name(TOPIC_NAME)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
