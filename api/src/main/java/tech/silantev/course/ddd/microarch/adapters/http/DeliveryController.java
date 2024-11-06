package tech.silantev.course.ddd.microarch.adapters.http;

import an.awesome.pipelinr.Pipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import tech.silantev.course.ddd.microarch.adapters.http.controllers.DefaultApi;
import tech.silantev.course.ddd.microarch.adapters.http.models.Courier;
import tech.silantev.course.ddd.microarch.adapters.http.models.Location;
import tech.silantev.course.ddd.microarch.adapters.http.models.Order;
import tech.silantev.course.ddd.microarch.usecases.commands.CreateOrderCommand;
import tech.silantev.course.ddd.microarch.usecases.queries.GetAllCouriersQuery;
import tech.silantev.course.ddd.microarch.usecases.queries.GetAllCreatedAndAssignedOrdersQuery;

import java.util.List;
import java.util.UUID;

@RestController
public class DeliveryController implements DefaultApi {

    private static final Logger LOG = LoggerFactory.getLogger(DeliveryController.class);
    private final Pipeline pipeline;

    @Autowired
    public DeliveryController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public ResponseEntity<Void> createOrder() {
        UUID id = UUID.randomUUID();
        String street = "Street #" + UUID.randomUUID();
        var result = pipeline.send(new CreateOrderCommand(id, street));
        if (result.isError()) {
            result.recoverError(this::printErrorToLog);
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<Courier>> getCouriers() {
        var result = pipeline.send(new GetAllCouriersQuery());
        if (result.isError()) {
            result.recoverError(this::printErrorToLog);
            return ResponseEntity.internalServerError().build();
        }
        List<Courier> couriers = result.discardError().get().stream()
                .map(courier -> {
                    Location location = new Location(courier.getLocation().getX(), courier.getLocation().getY());
                    return new Courier(courier.getId(), courier.getName(), location);
                }).toList();
        return ResponseEntity.ok(couriers);
    }

    @Override
    public ResponseEntity<List<Order>> getOrders() {
        var result = pipeline.send(new GetAllCreatedAndAssignedOrdersQuery());
        if (result.isError()) {
            result.recoverError(this::printErrorToLog);
            return ResponseEntity.internalServerError().build();
        }
        List<Order> orders = result.discardError().get().stream()
                .map(order -> {
                    Location location = new Location(order.getLocation().getX(), order.getLocation().getY());
                    return new Order(order.getId(), location);
                }).toList();
        return ResponseEntity.ok(orders);
    }

    private <T> T printErrorToLog(String errorMessage) {
        LOG.error(errorMessage);
        return null;
    }
}
