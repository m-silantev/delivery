package tech.silantev.course.ddd.microarch.domain.order.aggregate.events;

import an.awesome.pipelinr.Notification;

import java.util.UUID;

public record OrderCompletedEvent(
        UUID orderId
) implements Notification {
}
