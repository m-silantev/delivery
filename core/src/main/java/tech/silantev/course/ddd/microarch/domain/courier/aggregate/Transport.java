package tech.silantev.course.ddd.microarch.domain.courier.aggregate;

import com.github.sviperll.result4j.Result;
import tech.silantev.course.ddd.microarch.domain.sharedkernel.Location;

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

    public static Result<Transport, String> fromIdSafe(int id) {
        Optional<Transport> found = list().stream().filter(transport -> transport.getId() == id).findAny();
        return found.map(Result::<Transport, String>success)
                .orElseGet(() -> Result.error("Id " + id + " is incorrect."));
    }

    public static Transport fromId(int id) {
        return fromIdSafe(id).throwError(IllegalArgumentException::new);
    }

    public static Result<Transport, String> fromName(String name) {
        Optional<Transport> found = list().stream().filter(transport -> transport.getName().equalsIgnoreCase(name)).findAny();
        return found.map(Result::<Transport, String>success)
                .orElseGet(() -> Result.error("Name " + name + " is incorrect."));
    }

    public Location moveTowardPreferablyByX(Location from, Location to) {
        MoveResult movementByX = calculateOneStepWithReminder(from.getX(), to.getX(), speed);
        if (movementByX.unusedSteps() == 0) {
            return Location.create(movementByX.newPoint(), from.getY());
        }
        MoveResult movementByY = calculateOneStepWithReminder(from.getY(), to.getY(), movementByX.unusedSteps());
        return Location.create(movementByX.newPoint(), movementByY.newPoint());
    }

    public Location moveTowardPreferablyByY(Location from, Location to) {
        MoveResult movementByY = calculateOneStepWithReminder(from.getY(), to.getY(), speed);
        if (movementByY.unusedSteps() == 0) {
            return Location.create(from.getX(), movementByY.newPoint());
        }
        MoveResult movementByX = calculateOneStepWithReminder(from.getX(), to.getX(), movementByY.unusedSteps());
        return Location.create(movementByX.newPoint(), movementByY.newPoint());
    }

    private MoveResult calculateOneStepWithReminder(int pointFrom, int pointTo, int speed) {
        if (pointFrom == pointTo) {
            return new MoveResult(pointTo, speed);
        }
        if (pointFrom > pointTo) {
            int newPoint = pointFrom - speed;
            if (newPoint >= pointTo) {
                return new MoveResult(newPoint, 0);
            }
            return new MoveResult(pointTo, pointTo - newPoint);
        }
        // else pointFrom < pointTo
        int newPoint = pointFrom + speed;
        if (newPoint <= pointTo) {
            return new MoveResult(newPoint, 0);
        }
        return new MoveResult(pointTo, newPoint - pointTo);
    }

    private record MoveResult(int newPoint, int unusedSteps) {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }
}
