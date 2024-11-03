package tech.silantev.course.ddd.microarch.usecases.commands;

import an.awesome.pipelinr.Command;
import com.github.sviperll.result4j.Result;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Courier;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;
import tech.silantev.course.ddd.microarch.ports.CourierRepository;
import tech.silantev.course.ddd.microarch.ports.OrderRepository;

import java.util.List;
import java.util.Optional;

public class MoveCouriersHandler implements Command.Handler<MoveCouriersCommand, Result<Void, String>> {

    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;

    public MoveCouriersHandler(OrderRepository orderRepository, CourierRepository courierRepository) {
        this.orderRepository = orderRepository;
        this.courierRepository = courierRepository;
    }

    @Override
    public Result<Void, String> handle(MoveCouriersCommand command) {
        List<Order> assignedOrders = orderRepository.getAllAssigned().throwError(RuntimeException::new);
        assignedOrders.stream().filter(order -> order.getCourierId().isPresent()).forEach(order -> {
            Optional<Courier> optCourier = courierRepository.getById(order.getCourierId().get()).discardError();
            if (optCourier.isEmpty()) {
                return;
            }
            Courier courier = optCourier.get();
            if (courier.getDistanceTo(order.getLocation()) != 0) {
                courier.makeOneStepTo(order.getLocation());
                return;
            } // else complete order
            order.complete().throwError(RuntimeException::new);
            courier.setFree();
        });
        return Result.success(null);
    }
}
