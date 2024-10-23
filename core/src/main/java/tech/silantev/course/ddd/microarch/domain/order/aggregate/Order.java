package tech.silantev.course.ddd.microarch.domain.order.aggregate;

import com.github.sviperll.result4j.Result;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Courier;
import tech.silantev.course.ddd.microarch.domain.sharedkernel.Location;

import java.util.UUID;

public class Order {

    private final UUID id;
    private final Location location;
    private OrderStatus status;
    private UUID courierId;

    private Order(UUID id, Location location) {
        this.id = id;
        this.location = location;
    }

    public static Order create(UUID id, Location location) {
        Order order = new Order(id, location);
        order.setStatus(OrderStatus.CREATED);
        return order;
    }

    public void assignCourier(Courier courier) {
        setCourierId(courier.getId());
        setStatus(OrderStatus.ASSIGNED);
    }

    public Result<Order, String> complete() {
        if (getStatus() != OrderStatus.ASSIGNED) {
            return Result.error("Order hasn't been assigned and therefore cannot be completed");
        }
        setStatus(OrderStatus.COMPLETED);
        return Result.success(this);
    }

    public UUID getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public OrderStatus getStatus() {
        return status;
    }

    private void setStatus(OrderStatus status) {
        this.status = status;
    }

    public UUID getCourierId() {
        return courierId;
    }

    private void setCourierId(UUID courierId) {
        this.courierId = courierId;
    }
}
