package tech.silantev.course.ddd.microarch.domain.courier.aggregate;

import com.github.sviperll.result4j.Result;
import org.junit.jupiter.api.Test;
import tech.silantev.course.ddd.microarch.domain.sharedkernel.Location;

import static org.junit.jupiter.api.Assertions.*;

class CourierTests {

    /**
     * Из точки (1, 1) курьер на машине за один шаг должен переместиться в точку (2, 3)
     */
    @Test
    public void testOneStepMovementByCar() {
        // given
        Location startLocation = Location.create(1, 1);
        Transport transport = Transport.CAR;
        Courier courier = Courier.create("anyString", transport, startLocation);
        Location orderLocation = Location.create(2, 5);
        // when
        courier.makeOneStepTo(orderLocation);
        // then
        assertEquals(Location.create(2, 3), courier.getLocation());
    }

    @Test
    public void newCourierShouldBeCreatedWithStatusFree() {
        // given
        String name = "anyString";
        Transport transport = Transport.PEDESTRIAN;
        Location location = Location.createRandom();
        // when
        Courier courier = Courier.create(name, transport, location);
        // then
        assertEquals(CourierStatus.FREE, courier.getStatus());
    }

    @Test
    public void setBusyMethodShouldChangeStatusToBusy() {
        // given
        Courier courier = Courier.create("anyString", Transport.BICYCLE, Location.createRandom());
        // when
        Result<Courier, String> result = courier.setBusy();
        // then
        assertFalse(result.isError());
        assertEquals(CourierStatus.BUSY, courier.getStatus());
    }

    @Test
    public void setFreeMethodShouldChangeStatusToFree() {
        // given
        Courier courier = Courier.create("anyString", Transport.BICYCLE, Location.createRandom());
        courier.setBusy();
        // when
        Result<Courier, String> result = courier.setFree();
        // then
        assertFalse(result.isError());
        assertEquals(CourierStatus.FREE, courier.getStatus());
    }

    @Test
    public void courierIsProhibitedToChangeStatusFromFreeToFree() {
        // given
        Courier courier = Courier.create("anyString", Transport.BICYCLE, Location.createRandom());
        // when
        Result<Courier, String> result = courier.setFree();
        // then
        assertTrue(result.isError());
        assertEquals(CourierStatus.FREE, courier.getStatus());
    }

    @Test
    public void courierOnBicycleShouldGetFromCornerToCornerForNineSteps() {
        // given
        Location from = Location.create(1, 1);
        Location to = Location.create(10, 10);
        Transport transport = Transport.BICYCLE;
        Courier courier = Courier.create("anyString", transport, from);
        // when
        double time = courier.calculateTimeTo(to);
        // then
        assertEquals(9, time);
    }
}