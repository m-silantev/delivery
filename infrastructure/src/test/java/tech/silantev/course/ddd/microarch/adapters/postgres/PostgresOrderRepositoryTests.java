package tech.silantev.course.ddd.microarch.adapters.postgres;

import com.github.sviperll.result4j.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.silantev.course.ddd.microarch.adapters.SpringBootStarter;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Courier;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;
import tech.silantev.course.ddd.microarch.ports.CourierRepository;
import tech.silantev.course.ddd.microarch.ports.OrderRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(classes = SpringBootStarter.class)
class PostgresOrderRepositoryTests extends PostgresBaseTest {

    @Autowired
    OrderRepository repository;
    @Autowired
    CourierRepository courierRepository;

    private Courier createRandomCourierAndSaveItToDb() {
        Courier courier = createRandomCourier();
        return courierRepository.add(courier).throwError(RuntimeException::new);
    }

    @Test
    public void repositoryShouldSuccessfullySaveOrderIntoDb() {
        // given
        Order order = createRandomOrder();
        // when
        Result<Order, Exception> result = repository.add(order);
        // then
        assertFalse(result.isError(), () -> result.throwError(RuntimeException::new).toString());
    }

    @Test
    public void repositoryShouldSuccessfullyReadSavedOrderFromDb() {
        // given
        Order order = createRandomOrder();
        repository.add(order);
        UUID orderId = order.getId();
        // when
        Result<Order, Exception> result = repository.getById(orderId);
        // then
        Order orderFromDb = result.throwError(RuntimeException::new);
        assertEquals(order.getId(), orderFromDb.getId());
        assertEquals(order.getLocation(), orderFromDb.getLocation());
        assertEquals(order.getStatus(), orderFromDb.getStatus());
        assertEquals(order.getCourierId(), orderFromDb.getCourierId());
    }

    @Test
    public void repositoryShouldSuccessfullyUpdateOrder() {
        // given
        Order order = createRandomOrder();
        repository.add(order);
        order.assignCourier(createRandomCourierAndSaveItToDb());
        // when
        Result<Order, Exception> result = repository.update(order);
        // then
        Order orderFromDb = result.throwError(RuntimeException::new);
        assertEquals(order.getId(), orderFromDb.getId());
        assertEquals(order.getLocation(), orderFromDb.getLocation());
        assertEquals(order.getStatus(), orderFromDb.getStatus());
        assertEquals(order.getCourierId(), orderFromDb.getCourierId());
    }

    @Test
    public void repositoryShouldSuccessfullyRetrieveAllCreatedCouriers() {
        // given
        Order createdOrder1 = createRandomOrder();
        repository.add(createdOrder1);
        Order createdOrder2 = createRandomOrder();
        repository.add(createdOrder2);
        Order assignedOrder = createRandomOrder();
        assignedOrder.assignCourier(createRandomCourierAndSaveItToDb());
        repository.add(assignedOrder);
        // when
        Result<List<Order>, Exception> result = repository.getAllCreated();
        // then
        List<Order> orders = result.throwError(RuntimeException::new);
        assertEquals(2, orders.size());
        assertEquals(createdOrder1, orders.get(0));
        assertEquals(createdOrder2, orders.get(1));
    }

    @Test
    public void repositoryShouldSuccessfullyRetrieveAllAssignedCouriers() {
        // given
        Order createdOrder = createRandomOrder();
        repository.add(createdOrder);
        Order assignedOrder1 = createRandomOrder();
        assignedOrder1.assignCourier(createRandomCourierAndSaveItToDb());
        repository.add(assignedOrder1);
        Order assignedOrder2 = createRandomOrder();
        assignedOrder2.assignCourier(createRandomCourierAndSaveItToDb());
        repository.add(assignedOrder2);
        // when
        Result<List<Order>, Exception> result = repository.getAllAssigned();
        // then
        List<Order> orders = result.throwError(RuntimeException::new);
        assertEquals(2, orders.size());
        assertEquals(assignedOrder1, orders.get(0));
        assertEquals(assignedOrder2, orders.get(1));
    }
}
