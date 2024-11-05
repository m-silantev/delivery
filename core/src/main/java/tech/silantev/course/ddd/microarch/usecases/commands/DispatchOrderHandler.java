package tech.silantev.course.ddd.microarch.usecases.commands;

import an.awesome.pipelinr.Command;
import com.github.sviperll.result4j.Result;
import org.springframework.stereotype.Component;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Courier;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;
import tech.silantev.course.ddd.microarch.domain.services.DispatchService;
import tech.silantev.course.ddd.microarch.ports.CourierRepository;
import tech.silantev.course.ddd.microarch.ports.OrderRepository;

import java.util.List;

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
        Result<List<Courier>, Exception> resultFreeCouriers = courierRepository.getAllFree();
        if (resultFreeCouriers.isError()) {
            return resultFreeCouriers.<Order>map(list -> null).mapError(Exception::getMessage);
        }
        Result<List<Order>, Exception> resultCreatedOrders = orderRepository.getAllCreated();
        if (resultCreatedOrders.isError()) {
            return resultCreatedOrders.<Order>map(list -> null).mapError(Exception::getMessage);
        }
        List<Order> orders = resultCreatedOrders.discardError().get();
        if (orders.isEmpty()) {
            return Result.error("There are no orders to dispatch.");
        }
        Order order = orders.getFirst();
        Result<Courier, String> result = dispatchService.dispatch(order, resultFreeCouriers.discardError().get());
        return result.map(courier -> order);
    }
}
