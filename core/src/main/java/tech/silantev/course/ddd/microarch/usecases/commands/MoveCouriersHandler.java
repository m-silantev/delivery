package tech.silantev.course.ddd.microarch.usecases.commands;

import an.awesome.pipelinr.Command;
import com.github.sviperll.result4j.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Courier;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;
import tech.silantev.course.ddd.microarch.ports.CourierRepository;
import tech.silantev.course.ddd.microarch.ports.OrderRepository;

import java.util.List;

@Slf4j
@Component
public class MoveCouriersHandler implements Command.Handler<MoveCouriersCommand, Result<Void, String>> {

    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;

    @Autowired
    public MoveCouriersHandler(OrderRepository orderRepository, CourierRepository courierRepository) {
        this.orderRepository = orderRepository;
        this.courierRepository = courierRepository;
    }

    @Override
    public Result<Void, String> handle(MoveCouriersCommand command) {
        Result<List<Order>, Exception> resultAllAssignedOrders = orderRepository.getAllAssigned();
        if (resultAllAssignedOrders.isError()) {
            return resultAllAssignedOrders.mapError(Exception::getMessage).map(orders -> null);
        }
        for (Order order : resultAllAssignedOrders.discardError().get()) {
            if (order.getCourierId().isEmpty()) {
                String message = "Order with ASSIGNED status and no courierId set was found, " + order;
                log.warn(message);
                return Result.error(message);
            }
            Result<Courier, Exception> resultCourier = courierRepository.getById(order.getCourierId().get());
            if (resultCourier.isError()) {
                return resultCourier.mapError(exception -> {
                    log.error(exception.getMessage());
                    return exception.getMessage();
                }).map(courier -> null);
            }
            Courier courier = resultCourier.discardError().get();
            if (courier.getDistanceTo(order.getLocation()) != 0) {
                courier.makeOneStepTo(order.getLocation());
                Result<Courier, Exception> resultUpdateCourier = courierRepository.update(courier);
                if (resultUpdateCourier.isError()) {
                    return resultUpdateCourier.mapError(exception -> {
                        log.error(exception.getMessage());
                        return exception.getMessage();
                    }).map(_courier -> null);
                }
                log.info("Courier {} made a step forward order location", courier);
                continue;
            } // else complete order
            Result<Order, String> resultOfCompleteOrder = order.complete();
            if (resultOfCompleteOrder.isError()) {
                return resultOfCompleteOrder.mapError(message -> {
                    log.error(message);
                    return message;
                }).map(_order -> null);
            }
            Result<Order, Exception> resultUpdate = orderRepository.update(order);
            if (resultUpdate.isError()) {
//                log.error();
            }
            courier.setFree();
        }
        return Result.success(null);
    }
}
