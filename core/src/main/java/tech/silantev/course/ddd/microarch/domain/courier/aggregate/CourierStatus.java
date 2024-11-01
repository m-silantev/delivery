package tech.silantev.course.ddd.microarch.domain.courier.aggregate;

import com.github.sviperll.result4j.Result;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class CourierStatus {

    public static final CourierStatus FREE = new CourierStatus(1, "FREE");
    public static final CourierStatus BUSY = new CourierStatus(2, "BUSY");

    @EqualsAndHashCode.Include
    private final int id;
    private final String name;

    private CourierStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<CourierStatus> list() {
        return List.of(FREE, BUSY);
    }

    public static Result<CourierStatus, String> fromIdSafe(int id) {
        Optional<CourierStatus> found = list().stream().filter(courierStatus -> courierStatus.getId() == id).findAny();
        return found.map(Result::<CourierStatus, String>success)
                .orElseGet(() -> Result.error("Id " + id + " is incorrect."));
    }

    public static CourierStatus fromId(int id) {
        return fromIdSafe(id).throwError(IllegalArgumentException::new);
    }

    public static Result<CourierStatus, String> fromName(String name) {
        Optional<CourierStatus> found = list().stream().filter(courierStatus -> courierStatus.getName().equalsIgnoreCase(name)).findAny();
        return found.map(Result::<CourierStatus, String>success)
                .orElseGet(() -> Result.error("Name " + name + " is incorrect."));
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
