package tech.silantev.course.ddd.microarch.domain.order.aggregate;

import org.junit.jupiter.api.Test;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Courier;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Transport;
import tech.silantev.course.ddd.microarch.domain.sharedkernel.Location;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OrderTests {

    @Test
    public void whenOrderIsCreatedItShouldHaveCreatedStatus() {
        // given
        UUID id = UUID.randomUUID();
        Location location = Location.createRandom();
        // when
        Order order = Order.create(id, location);
        // then
        assertEquals(OrderStatus.CREATED, order.getStatus());
    }

    @Test
    public void whenOrderIsCreatedItShouldNotHaveCourierId() {
        // given
        UUID id = UUID.randomUUID();
        Location location = Location.createRandom();
        // when
        Order order = Order.create(id, location);
        // then
        assertNull(order.getCourierId());
    }

    @Test
    public void whenOrderIsAssignedItShouldHaveCourierIdAndStatusAssigned() {
        // given
        Order order = Order.create(UUID.randomUUID(), Location.createRandom());
        Courier courier = Courier.create("anyString", Transport.BICYCLE, Location.createRandom());
        // when
        order.assignCourier(courier);
        // then
        assertEquals(OrderStatus.ASSIGNED, order.getStatus());
        assertEquals(courier.getId(), order.getCourierId());
    }

    @Test
    public void itIsImpossibleToCompleteOrderIfItWasNotAssigned() {
        // given
        Order order = Order.create(UUID.randomUUID(), Location.createRandom());
        OrderStatus expected = order.getStatus();
        // when
        order.complete();
        // then
        assertEquals(expected, order.getStatus());
    }

    @Test
    public void whenOrderIsCompletedItShouldStatusCompleted() {
        // given
        Order order = Order.create(UUID.randomUUID(), Location.createRandom());
        Courier courier = Courier.create("anyString", Transport.BICYCLE, Location.createRandom());
        order.assignCourier(courier);
        // when
        order.complete();
        // then
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
    }
}
