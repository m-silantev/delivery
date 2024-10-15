package tech.silantev.course.ddd.microarch.domain.courier.aggregate;

import com.github.sviperll.result4j.Result;

import java.util.List;
import java.util.Optional;

public class Transport {

    public static final Transport PEDESTRIAN = new Transport(1, "PEDESTRIAN", 1);
    public static final Transport BICYCLE = new Transport(2, "BICYCLE", 2);
    public static final Transport CAR = new Transport(3, "CAR", 3);

    private final int id;
    private final String name;
    private final int speed;

    private Transport(int id, String name, int speed) {
        this.id = id;
        this.name = name;
        this.speed = speed;
    }

    public static List<Transport> list() {
        return List.of(PEDESTRIAN, BICYCLE, CAR);
    }

    public static Result<Transport, String> fromId(int id) {
        Optional<Transport> found = list().stream().filter(transport -> transport.id() == id).findAny();
        return found.map(Result::<Transport, String>success)
                .orElseGet(() -> Result.error("Id " + id + " is incorrect."));
    }

    public static Result<Transport, String> fromName(String name) {
        Optional<Transport> found = list().stream().filter(transport -> transport.name().equalsIgnoreCase(name)).findAny();
        return found.map(Result::<Transport, String>success)
                .orElseGet(() -> Result.error("Name " + name + " is incorrect."));
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public int speed() {
        return speed;
    }
}
