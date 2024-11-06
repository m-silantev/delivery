package tech.silantev.course.ddd.microarch.usecases.commands;

import an.awesome.pipelinr.Command;
import com.github.sviperll.result4j.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;
import tech.silantev.course.ddd.microarch.domain.sharedkernel.Location;
import tech.silantev.course.ddd.microarch.ports.OrderRepository;

import java.util.UUID;

@Slf4j
@Component
public class CreateOrderHandler implements Command.Handler<CreateOrderCommand, Result<Order, String>> {

    private final OrderRepository orderRepository;

    @Autowired
    public CreateOrderHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Result<Order, String> handle(CreateOrderCommand command) {
        try {
            UUID basketId = command.basketId();
            // Location location = geoService.getLocation(command.street());
            Location location = Location.createRandom();
            Order order = Order.create(basketId, location);
            orderRepository.add(order);
            log.info("Order created successfully, {}", order);
            return Result.success(order);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }
}
