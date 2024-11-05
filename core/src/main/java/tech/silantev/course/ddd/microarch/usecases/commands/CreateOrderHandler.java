package tech.silantev.course.ddd.microarch.usecases.commands;

import an.awesome.pipelinr.Command;
import com.github.sviperll.result4j.Result;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;
import tech.silantev.course.ddd.microarch.domain.sharedkernel.Location;

import java.util.UUID;

public class CreateOrderHandler implements Command.Handler<CreateOrderCommand, Result<Order, String>> {
    @Override
    public Result<Order, String> handle(CreateOrderCommand command) {
        UUID basketId = command.basketId();
        // Location location = geoService.getLocation(command.street());
        Location location = Location.createRandom();
        Order order = Order.create(basketId, location);
        return Result.success(order);
    }
}
