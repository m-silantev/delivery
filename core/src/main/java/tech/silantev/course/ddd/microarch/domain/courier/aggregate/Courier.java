package tech.silantev.course.ddd.microarch.domain.courier.aggregate;

import com.github.sviperll.result4j.Result;
import tech.silantev.course.ddd.microarch.domain.sharedkernel.Location;

import java.util.UUID;

public class Courier {

    private final UUID id;
    private final String name;
    private final Transport transport;
    private Location location;
    private CourierStatus status;

    private Courier(UUID id, String name, Transport transport, Location location, CourierStatus status) {
        this.id = id;
        this.name = name;
        this.transport = transport;
        this.location = location;
        this.status = status;
    }

    public static Courier create(String name, Transport transport, Location location) {
        UUID id = UUID.randomUUID();
        return new Courier(id, name, transport, location, CourierStatus.FREE);
    }

    public Result<Courier, String> setBusy() {
        if (status == CourierStatus.BUSY) {
            return Result.error("Courier is already Busy");
        }
        setStatus(CourierStatus.BUSY);
        return Result.success(this);
    }

    public Result<Courier, String> setFree() {
        if (status == CourierStatus.FREE) {
            return Result.error("Courier is already Free");
        }
        setStatus(CourierStatus.FREE);
        return Result.success(this);
    }

    public int getDistanceTo(Location location) {
        return location.distanceBetween(location);
    }

    public void makeOneStepTo(Location orderLocation) {
        Location courierLocation = location;
        int deltaX = Math.abs(orderLocation.getX() - courierLocation.getX());
        int deltaY = Math.abs(orderLocation.getY() - courierLocation.getY());
        if (deltaX == 0 && deltaY == 0) {
            return;
        }
        Location moved = deltaX != 0 && deltaX < deltaY
                ? transport.moveTowardPreferablyByX(courierLocation, orderLocation)
                : transport.moveTowardPreferablyByY(courierLocation, orderLocation);
        setLocation(moved);
    }

    public double calculateTimeTo(Location location) {
        double distance = this.location.distanceBetween(location);
        return distance / transport.getSpeed();
    }

    private void setStatus(CourierStatus status) {
        this.status = status;
    }

    private void setLocation(Location location) {
        this.location = location;
    }

    public UUID getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public CourierStatus getStatus() {
        return status;
    }

    public Transport getTransport() {
        return transport;
    }
}
