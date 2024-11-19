package tech.silantev.course.ddd.microarch.adapters.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.events.OrderCompletedEvent;
import tech.silantev.course.ddd.microarch.ports.NotificationService;

@Slf4j
@Component
public class NotificationServiceAdapter implements NotificationService {

    static final String TOPIC_NAME = "order.status.changed";
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public NotificationServiceAdapter(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendOrderCompleted(OrderCompletedEvent domainEvent) {
        OrderStatusChangedIntegrationEvent event = OrderStatusChangedIntegrationEvent.newBuilder()
                .setOrderId(domainEvent.orderId().toString())
                .setOrderStatus(OrderStatus.Completed)
                .build();
        try {
            String json = JsonFormat.printer().print(event);
            this.kafkaTemplate.send(TOPIC_NAME, json);
            log.debug("{} sent to kafka", json);
        } catch (InvalidProtocolBufferException e) {
            log.error(e.getMessage(), e);
        }
    }
}
