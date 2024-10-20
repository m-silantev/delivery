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

    public void moveTo(Order order) {
        Location orderLocation = order.getLocation();
        Location courierLocation = location;
        if (orderLocation.getX() != courierLocation.getX()) {
            setLocation(moveToByX(orderLocation));
            moveTo(order);
        }
        if (orderLocation.getY() != courierLocation.getY()) {
            setLocation(moveToByY(orderLocation));
            moveTo(order);
        }
    }

    private Location moveToByX(Location orderLocation) {
        Location courierLocation = location;
        if (orderLocation.getX() == courierLocation.getX()) {
            return courierLocation;
        }
        int step = courierLocation.getX() * transport.speed();
        if (orderLocation.getX() > courierLocation.getX()) {
            return Location.create(Math.min(courierLocation.getX() + step, orderLocation.getX()), orderLocation.getY());
        }
        // else <
        return Location.create(Math.max(courierLocation.getX() - step, orderLocation.getX()), orderLocation.getY());
    }

    private Location moveToByY(Location orderLocation) {
        Location courierLocation = location;
        if (orderLocation.getY() == courierLocation.getY()) {
            return courierLocation;
        }
        int step = courierLocation.getY() * transport.speed();
        if (orderLocation.getY() > courierLocation.getY()) {
            return Location.create(orderLocation.getX(), Math.min(courierLocation.getY() + step, orderLocation.getY()));
        }
        // else <
        return Location.create(orderLocation.getX(), Math.min(courierLocation.getY() - step, orderLocation.getY()));
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
