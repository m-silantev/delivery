package tech.silantev.course.ddd.microarch.domain.courier.aggregate;

import org.junit.jupiter.api.Test;
import tech.silantev.course.ddd.microarch.domain.sharedkernel.Location;

import static org.junit.jupiter.api.Assertions.*;

class CourierTests {

    @Test
    public void test() {
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
}