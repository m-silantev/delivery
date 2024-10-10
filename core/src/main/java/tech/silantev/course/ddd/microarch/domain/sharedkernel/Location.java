package tech.silantev.course.ddd.microarch.domain.sharedkernel;

import java.util.concurrent.ThreadLocalRandom;

public record Location(int x, int y) {

    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 10;

    public Location {
        checkInRange(x, "x");
        checkInRange(y, "y");
    }

    private void checkInRange(int value, String name) {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException(name + "=" + value + " is not in allowed range");
        }
    }

    public static Location random() {
        int x = ThreadLocalRandom.current().nextInt(MIN_VALUE, MAX_VALUE);
        int y = ThreadLocalRandom.current().nextInt(MIN_VALUE, MAX_VALUE);
        return new Location(x, y);
    }

    public int distanceBetween(Location another) {
        return Math.abs(x - another.x) + Math.abs(y - another.y);
    }
}
