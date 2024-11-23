package tech.silantev.course.ddd.microarch.ports;

import tech.silantev.course.ddd.microarch.domain.order.aggregate.events.OrderCompletedEvent;

public interface NotificationService {
    void sendOrderCompleted(OrderCompletedEvent event);
}
