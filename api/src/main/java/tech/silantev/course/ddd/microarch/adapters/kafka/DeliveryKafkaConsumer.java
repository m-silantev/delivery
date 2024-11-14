package tech.silantev.course.ddd.microarch.adapters.kafka;

import an.awesome.pipelinr.Pipeline;
import com.google.protobuf.util.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tech.silantev.course.ddd.microarch.usecases.commands.CreateOrderCommand;

import java.util.UUID;

@Slf4j
@Component
public class DeliveryKafkaConsumer {

    private final Pipeline pipeline;

    @Autowired
    public DeliveryKafkaConsumer(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @KafkaListener(groupId = "group-id-1", topics = "basket.confirmed")
    public void listen(String json) {
        try {
            BasketConfirmedIntegrationEvent.Builder builder = BasketConfirmedIntegrationEvent.newBuilder();
            JsonFormat.parser().merge(json, builder);
            BasketConfirmedIntegrationEvent event = builder.build();
            UUID basketId = UUID.fromString(event.getBasketId());
            String street = event.getAddress().getStreet();
            pipeline.send(new CreateOrderCommand(basketId, street));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
