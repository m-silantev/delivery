package tech.silantev.course.ddd.microarch.usecases.commands;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;
import com.github.sviperll.result4j.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Courier;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.events.OrderCompletedEvent;
import tech.silantev.course.ddd.microarch.ports.CourierRepository;
import tech.silantev.course.ddd.microarch.ports.OrderRepository;
import tech.silantev.course.ddd.microarch.usecases.UseCaseException;

import java.util.List;
import java.util.function.Function;

@Slf4j
@Component
public class MoveCouriersHandler implements Command.Handler<MoveCouriersCommand, Result<Void, String>> {

    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    private final Pipeline pipeline;

    @Autowired
    public MoveCouriersHandler(OrderRepository orderRepository, CourierRepository courierRepository, Pipeline pipeline) {
        this.orderRepository = orderRepository;
        this.courierRepository = courierRepository;
        this.pipeline = pipeline;
    }

    @Override
    public Result<Void, String> handle(MoveCouriersCommand command) {
        try {
            List<Order> orders = orderRepository.getAllAssigned().throwError(Function.identity());
            for (Order order : orders) {
                if (order.getCourierId().isEmpty()) {
                    throw new UseCaseException("Order in ASSIGNED status and no courierId set was found, " + order);
                }
                Courier courier = courierRepository.getById(order.getCourierId().get()).throwError(Function.identity());
                if (courier.getDistanceTo(order.getLocation()) != 0) {
                    courier.makeOneStepTo(order.getLocation());
                    courierRepository.update(courier).throwError(Function.identity());
                    log.info("Courier {} made a step forward order location", courier);
                    continue;
                } // else complete order
                order.complete().throwError(UseCaseException::new);
                courier.setFree().throwError(UseCaseException::new);
                orderRepository.update(order).throwError(Function.identity());
                courierRepository.update(courier).throwError(Function.identity());
                pipeline.send(new OrderCompletedEvent(order.getId()));
                log.info("Courier {} complete an order {}", courier, order);
            }
            return Result.success(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }
}
