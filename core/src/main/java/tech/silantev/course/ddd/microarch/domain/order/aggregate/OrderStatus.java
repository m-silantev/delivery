package tech.silantev.course.ddd.microarch.domain.order.aggregate;

import com.github.sviperll.result4j.Result;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class OrderStatus {

    public static final OrderStatus CREATED = new OrderStatus(1, "CREATED");
    public static final OrderStatus ASSIGNED = new OrderStatus(2, "ASSIGNED");
    public static final OrderStatus COMPLETED = new OrderStatus(3, "COMPLETED");

    @EqualsAndHashCode.Include
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
}
