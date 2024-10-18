package tech.silantev.course.ddd.microarch.domain.courier.aggregate;

import com.github.sviperll.result4j.Result;

import java.util.List;
import java.util.Optional;

public class CourierStatus {

    public static final CourierStatus FREE = new CourierStatus(1, "FREE");
    public static final CourierStatus BUSY = new CourierStatus(2, "BUSY");

    private final int id;
    private final String name;

    private CourierStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<CourierStatus> list() {
        return List.of(FREE, BUSY);
    }

    public static Result<CourierStatus, String> fromId(int id) {
        Optional<CourierStatus> found = list().stream().filter(courierStatus -> courierStatus.id() == id).findAny();
        return found.map(Result::<CourierStatus, String>success)
                .orElseGet(() -> Result.error("Id " + id + " is incorrect."));
    }

    public static Result<CourierStatus, String> fromName(String name) {
        Optional<CourierStatus> found = list().stream().filter(courierStatus -> courierStatus.name().equalsIgnoreCase(name)).findAny();
        return found.map(Result::<CourierStatus, String>success)
                .orElseGet(() -> Result.error("Name " + name + " is incorrect."));
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }
}
