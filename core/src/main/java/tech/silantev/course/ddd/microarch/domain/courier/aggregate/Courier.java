package tech.silantev.course.ddd.microarch.domain.courier.aggregate;

import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;
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

    public void setBusy() {
        setStatus(CourierStatus.BUSY);
    }

    public void setFree() {
        setStatus(CourierStatus.FREE);
    }

    public int getDistanceToOrder(Order order) {
        return order.getLocation().distanceBetween(location);
    }

    public void makeOneStepTo(Order order) {
        Location orderLocation = order.getLocation();
        Location courierLocation = location;
        int deltaX = Math.abs(orderLocation.getX() - courierLocation.getX());
        int deltaY = Math.abs(orderLocation.getY() - courierLocation.getY());
        if (deltaX == 0 && deltaY == 0) {
            return;
        }
        if (deltaX > deltaY) {
            moveToByX(orderLocation);
        }
        moveToByY(orderLocation);
    }

    private void moveToByX(Location orderLocation) {
        Location courierLocation = location;
        int newX = calculateOneStepCoordinate(orderLocation.getX(), courierLocation.getX());
        setLocation(Location.create(newX, courierLocation.getY()));
    }

    private void moveToByY(Location orderLocation) {
        Location courierLocation = location;
        int newY = calculateOneStepCoordinate(orderLocation.getY(), courierLocation.getY());
        setLocation(Location.create(courierLocation.getX(), newY));
    }

    private int calculateOneStepCoordinate(int pointFrom, int pointTo) {
        if (pointFrom == pointTo) {
            return pointFrom;
        }
        int step = pointFrom * transport.speed();
        if (pointFrom > pointTo) {
            return Math.max(pointFrom - step, pointTo);
        }
        // else <
        return Math.min(pointFrom + step, pointTo);
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
