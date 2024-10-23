package tech.silantev.course.ddd.microarch.domain.order.aggregate;

import com.github.sviperll.result4j.Result;
import org.junit.jupiter.api.Test;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Courier;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Transport;
import tech.silantev.course.ddd.microarch.domain.sharedkernel.Location;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        Result<Order, String> result = order.complete();
        // then
        assertTrue(result.isError());
        assertEquals(expected, order.getStatus());
    }

    @Test
    public void whenOrderIsCompletedItShouldHaveStatusCompleted() {
        // given
        Order order = Order.create(UUID.randomUUID(), Location.createRandom());
        Courier courier = Courier.create("anyString", Transport.BICYCLE, Location.createRandom());
        order.assignCourier(courier);
        // when
        Result<Order, String> result = order.complete();
        // then
        assertFalse(result.isError());
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
    }
}
