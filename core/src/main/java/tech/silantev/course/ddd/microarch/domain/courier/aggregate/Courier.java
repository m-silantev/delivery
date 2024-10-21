package tech.silantev.course.ddd.microarch.domain.courier.aggregate;

import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;
import tech.silantev.course.ddd.microarch.domain.sharedkernel.Location;

import java.util.Optional;
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

    public void setBusy() {
        setStatus(CourierStatus.BUSY);
    }

    public void setFree() {
        setStatus(CourierStatus.FREE);
    }

    public int getDistanceTo(Order order) {
        return getDistanceTo(order.getLocation());
    }

    public int getDistanceTo(Location location) {
        return location.distanceBetween(location);
    }

    public void makeOneStepTo(Order order) {
        makeOneStepTo(order.getLocation());
    }

    public void makeOneStepTo(Location orderLocation) {
        Location courierLocation = location;
        int deltaX = Math.abs(orderLocation.getX() - courierLocation.getX());
        int deltaY = Math.abs(orderLocation.getY() - courierLocation.getY());
        if (deltaX == 0 && deltaY == 0) {
            return;
        }
        if (deltaX < deltaY) {
            moveByX(orderLocation, transport.getSpeed())
                    .ifPresent(unusedSteps -> moveByY(orderLocation, unusedSteps));

        } else {
            moveByY(orderLocation, transport.getSpeed())
                    .ifPresent(unusedSteps -> moveByX(orderLocation, unusedSteps));
        }
    }

    Optional<Integer> moveByX(Location to, int speed) {
        Location from = location;
        MoveResult moveResult = calculateOneStepWithReminder(from.getX(), to.getX(), speed);
        setLocation(Location.create(moveResult.newPoint(), from.getY()));
        return moveResult.unusedSteps() == 0 ? Optional.empty() : Optional.of(moveResult.unusedSteps());
    }

    Optional<Integer> moveByY(Location to, int speed) {
        Location from = location;
        MoveResult moveResult = calculateOneStepWithReminder(from.getY(), to.getY(), speed);
        setLocation(Location.create(from.getX(), moveResult.newPoint()));
        return moveResult.unusedSteps() == 0 ? Optional.empty() : Optional.of(moveResult.unusedSteps());
    }

    MoveResult calculateOneStepWithReminder(int pointFrom, int pointTo, int speed) {
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

    record MoveResult(int newPoint, int unusedSteps) {

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
