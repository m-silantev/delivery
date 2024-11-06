package tech.silantev.course.ddd.microarch.usecases.commands;

import an.awesome.pipelinr.Command;
import com.github.sviperll.result4j.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Courier;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;
import tech.silantev.course.ddd.microarch.domain.services.DispatchService;
import tech.silantev.course.ddd.microarch.ports.CourierRepository;
import tech.silantev.course.ddd.microarch.ports.OrderRepository;
import tech.silantev.course.ddd.microarch.usecases.UseCaseException;

import java.util.List;
import java.util.function.Function;

@Slf4j
@Component
public class DispatchOrderHandler implements Command.Handler<DispatchOrderCommand, Result<Order, String>> {

    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    private final DispatchService dispatchService;

    public DispatchOrderHandler(OrderRepository orderRepository, CourierRepository courierRepository, DispatchService dispatchService) {
        this.orderRepository = orderRepository;
        this.courierRepository = courierRepository;
        this.dispatchService = dispatchService;
    }

    @Override
    public Result<Order, String> handle(DispatchOrderCommand command) {
        try {
            List<Courier> couriers = courierRepository.getAllFree().throwError(Function.identity());
            List<Order> orders = orderRepository.getAllCreated().throwError(Function.identity());
            if (orders.isEmpty()) {
                throw new UseCaseException("There are no orders in CREATED status to dispatch");
            }
            Order order = orders.getFirst();
            Courier courier = dispatchService.dispatch(order, couriers).throwError(UseCaseException::new);
            courier.setBusy().throwError(UseCaseException::new);
            orderRepository.update(order).throwError(Function.identity());
            courierRepository.update(courier).throwError(Function.identity());
            log.info("Order {} was successfully dispatched to courier {}", order, courier);
            return Result.success(order);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }
}
