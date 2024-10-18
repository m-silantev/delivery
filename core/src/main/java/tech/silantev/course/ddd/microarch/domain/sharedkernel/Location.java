package tech.silantev.course.ddd.microarch.domain.sharedkernel;


import com.github.sviperll.result4j.Result;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Location {

    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 10;

    private final int x;
    private final int y;

    private Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private static Result<Integer, String> checkInRange(int value, String name) {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            return Result.error(name + "=" + value + " is not in allowed range");
        }
        return Result.success(value);
    }

    public static Location create(int x, int y) {
        checkInRange(x, "x").throwError(IllegalArgumentException::new);
        checkInRange(y, "y").throwError(IllegalArgumentException::new);
        return new Location(x, y);
    }

    public static Result<Location, String> createSafe(int x, int y) {
        Result<Integer, String> checkResult = checkInRange(x, "x").andThen(checkInRange(y, "y"));
        if (checkResult.isError()) {
            return checkResult.map(coordinate -> null);
        }
        return Result.success(new Location(x, y));
    }

    public static Location createRandom() {
        int x = ThreadLocalRandom.current().nextInt(MIN_VALUE, MAX_VALUE);
        int y = ThreadLocalRandom.current().nextInt(MIN_VALUE, MAX_VALUE);
        return new Location(x, y);
    }

    public int distanceBetween(Location another) {
        return Math.abs(x - another.x) + Math.abs(y - another.y);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Location location = (Location) object;
        return x == location.x && y == location.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
