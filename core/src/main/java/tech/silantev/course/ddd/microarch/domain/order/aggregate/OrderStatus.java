package tech.silantev.course.ddd.microarch.domain.order.aggregate;

import com.github.sviperll.result4j.Result;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class OrderStatus {

    public static final OrderStatus CREATED = new OrderStatus(1, "CREATED");
    public static final OrderStatus ASSIGNED = new OrderStatus(2, "ASSIGNED");
    public static final OrderStatus COMPLETED = new OrderStatus(3, "COMPLETED");

    private final int id;
    private final String name;

    private OrderStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<OrderStatus> list() {
        return List.of(CREATED, ASSIGNED, COMPLETED);
    }

    public static Result<OrderStatus, String> fromIdSafe(int id) {
        Optional<OrderStatus> found = list().stream().filter(orderStatus -> orderStatus.id() == id).findAny();
        return found.map(Result::<OrderStatus, String>success)
                .orElseGet(() -> Result.error("Id " + id + " is incorrect."));
    }

    public static OrderStatus fromId(int id) {
        return fromIdSafe(id).throwError(IllegalArgumentException::new);
    }

    public static Result<OrderStatus, String> fromNameSafe(String name) {
        Optional<OrderStatus> found = list().stream().filter(orderStatus -> orderStatus.name().equalsIgnoreCase(name)).findAny();
        return found.map(Result::<OrderStatus, String>success)
                .orElseGet(() -> Result.error("Status " + name + " is incorrect."));
    }

    public static OrderStatus fromName(String name) {
        return fromNameSafe(name).throwError(IllegalArgumentException::new);
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        OrderStatus that = (OrderStatus) object;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
