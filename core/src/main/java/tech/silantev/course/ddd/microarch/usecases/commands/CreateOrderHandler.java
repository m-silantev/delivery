package tech.silantev.course.ddd.microarch.usecases.commands;

import an.awesome.pipelinr.Command;
import com.github.sviperll.result4j.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;
import tech.silantev.course.ddd.microarch.domain.sharedkernel.Location;
import tech.silantev.course.ddd.microarch.ports.GeoService;
import tech.silantev.course.ddd.microarch.ports.OrderRepository;

import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Component
public class CreateOrderHandler implements Command.Handler<CreateOrderCommand, Result<Order, String>> {

    private final OrderRepository orderRepository;
    private final GeoService geoService;

    @Autowired
    public CreateOrderHandler(OrderRepository orderRepository, GeoService geoService) {
        this.orderRepository = orderRepository;
        this.geoService = geoService;
    }

    @Override
    public Result<Order, String> handle(CreateOrderCommand command) {
        try {
            UUID basketId = command.basketId();
            Location location = geoService.getLocation(command.street()).throwError(Function.identity());
            Order order = Order.create(basketId, location);
            orderRepository.add(order).throwError(Function.identity());
            log.info("Order created successfully, {}", order);
            return Result.success(order);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }
}
