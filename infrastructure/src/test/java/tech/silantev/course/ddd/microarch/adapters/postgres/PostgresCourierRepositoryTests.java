package tech.silantev.course.ddd.microarch.adapters.postgres;

import com.github.sviperll.result4j.Result;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.silantev.course.ddd.microarch.adapters.SpringBootStarter;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Courier;
import tech.silantev.course.ddd.microarch.ports.CourierRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(classes = SpringBootStarter.class)
class PostgresCourierRepositoryTests extends PostgresBaseTest {

    @Autowired
    CourierRepository repository;

    @BeforeAll
    public static void setup() {
        PostgresContainer.getInstance().start();
    }

    @Test
    public void repositoryShouldSuccessfullySaveCourierIntoDb() {
        // given
        Courier courier = createRandomCourier();
        // when
        Result<Courier, Exception> result = repository.add(courier);
        // then
        assertFalse(result.isError(), () -> result.throwError(RuntimeException::new).toString());
    }

    @Test
    public void repositoryShouldSuccessfullyReadSavedCourierFromDb() {
        // given
        Courier courier = createRandomCourier();
        repository.add(courier);
        UUID courierId = courier.getId();
        // when
        Result<Courier, Exception> result = repository.getById(courierId);
        // then
        assertFalse(result.isError(), () -> result.throwError(RuntimeException::new).toString());
        Courier courierFromDb = result.throwError(RuntimeException::new);
        assertEquals(courier.getId(), courierFromDb.getId());
        assertEquals(courier.getName(), courierFromDb.getName());
        assertEquals(courier.getTransport(), courierFromDb.getTransport());
        assertEquals(courier.getLocation(), courierFromDb.getLocation());
        assertEquals(courier.getStatus(), courierFromDb.getStatus());
    }

    @Test
    public void repositoryShouldSuccessfullyUpdateCourier() {
        // given
        Courier courier = createRandomCourier();
        repository.add(courier);
        courier.setBusy().throwError(RuntimeException::new);
        // when
        Result<Courier, Exception> result = repository.update(courier);
        // then
        assertFalse(result.isError(), () -> result.throwError(RuntimeException::new).toString());
        Courier courierFromDb = result.throwError(RuntimeException::new);
        assertEquals(courier.getId(), courierFromDb.getId());
        assertEquals(courier.getName(), courierFromDb.getName());
        assertEquals(courier.getTransport(), courierFromDb.getTransport());
        assertEquals(courier.getLocation(), courierFromDb.getLocation());
        assertEquals(courier.getStatus(), courierFromDb.getStatus());
    }

    @Test
    public void repositoryShouldSuccessfullyRetrieveAllFreeCouriers() {
        // given
        Courier busyCourier = createRandomCourier();
        busyCourier.setBusy().throwError(RuntimeException::new);
        repository.add(busyCourier);
        Courier freeCourier1 = createRandomCourier();
        repository.add(freeCourier1);
        Courier freeCourier2 = createRandomCourier();
        repository.add(freeCourier2);
        // when
        Result<List<Courier>, Exception> result = repository.getAllFree();
        // then
        assertFalse(result.isError(), () -> result.throwError(RuntimeException::new).toString());
        List<Courier> couriers = result.throwError(RuntimeException::new);
        assertEquals(2, couriers.size());
        assertEquals(freeCourier1, couriers.get(0));
        assertEquals(freeCourier2, couriers.get(1));
    }
}
