package tech.silantev.course.ddd.microarch.domain.services;

import com.github.sviperll.result4j.Result;
import org.junit.jupiter.api.Test;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Courier;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Transport;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.OrderStatus;
import tech.silantev.course.ddd.microarch.domain.sharedkernel.Location;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DispatchServiceTests {

    DispatchService dispatchService = new DispatchServiceImpl();

    private Courier createPedestrianCourier(Location location) {
        return Courier.create(UUID.randomUUID().toString(), Transport.PEDESTRIAN, location);
    }

    private Courier createBicycleCourier(Location location) {
        return Courier.create(UUID.randomUUID().toString(), Transport.BICYCLE, location);
    }

    private Courier createCarCourier(Location location) {
        return Courier.create(UUID.randomUUID().toString(), Transport.CAR, location);
    }

    @Test
    public void orderMustBeInCreatedStatusToBeDispatched() {
        // given
        Order order = Order.create(UUID.randomUUID(), Location.createRandom());
        order.assignCourier(createCarCourier(Location.createRandom()));
        Courier targetCourier = Courier.create("targetCourier", Transport.BICYCLE, Location.createRandom());
        // when
        Result<Order, String> result = dispatchService.dispatch(order, List.of(targetCourier));
        //then
        assertTrue(result.isError());
        assertNotEquals(targetCourier.getId(), order.getCourierId());
    }

    @Test
    public void itIsImpossibleToDispatchOrderIfThereAreNoFreeCouriers() {
        // given
        Order order = Order.create(UUID.randomUUID(), Location.createRandom());
        Courier courier1 = createPedestrianCourier(Location.createRandom());
        courier1.setBusy();
        Courier courier2 = createPedestrianCourier(Location.createRandom());
        courier2.setBusy();
        List<Courier> busyCouriers = List.of(courier1, courier2);
        // when
        Result<Order, String> result = dispatchService.dispatch(order, busyCouriers);
        //then
        assertTrue(result.isError());
        assertNull(order.getCourierId());
    }

    @Test
    public void orderShouldBeAssignedToNearestCourierIfSpeedsAreEqual() {
        // given
        Order orderInFourthQuarter = Order.create(UUID.randomUUID(), Location.create(6, 6));
        Courier courierInFirstQuarter = createCarCourier(Location.create(1, 1));
        Courier courierInSecondQuarter = createCarCourier(Location.create(10, 1));
        Courier courierInThirdQuarter = createCarCourier(Location.create(1, 10));
        Courier courierInFourthQuarter = createCarCourier(Location.create(10, 10));
        List<Courier> couriers = List.of(courierInFirstQuarter, courierInSecondQuarter, courierInThirdQuarter, courierInFourthQuarter);
        // when
        Result<Order, String> result = dispatchService.dispatch(orderInFourthQuarter, couriers);
        //then
        assertFalse(result.isError());
        assertEquals(OrderStatus.ASSIGNED, orderInFourthQuarter.getStatus());
        assertEquals(courierInFourthQuarter.getId(), orderInFourthQuarter.getCourierId());
    }

    @Test
    public void orderShouldBeAssignedToFastestCourierIfDistancesAreEqual() {
        // given
        Location destination = Location.create(3, 9);
        Order order = Order.create(UUID.randomUUID(), destination);
        Courier pedestrianCourier = createPedestrianCourier(Location.create(1, 1));
        Courier bicycleCourier = createBicycleCourier(Location.create(1, 1));
        Courier carCourier = createCarCourier(Location.create(1, 1));
        List<Courier> couriers = List.of(pedestrianCourier, carCourier, bicycleCourier);
        // when
        Result<Order, String> result = dispatchService.dispatch(order, couriers);
        //then
        assertFalse(result.isError());
        assertEquals(OrderStatus.ASSIGNED, order.getStatus());
        assertEquals(carCourier.getId(), order.getCourierId());
    }
}