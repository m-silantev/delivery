package tech.silantev.course.ddd.microarch.usecases.eventhandlers;

import an.awesome.pipelinr.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.events.OrderCompletedEvent;
import tech.silantev.course.ddd.microarch.ports.NotificationService;

@Component
public class OrderCompletedDomainEventHandler implements Notification.Handler<OrderCompletedEvent> {

    private final NotificationService notificationService;

    @Autowired
    public OrderCompletedDomainEventHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void handle(OrderCompletedEvent event) {
        notificationService.sendOrderCompleted(event);
    }
}
