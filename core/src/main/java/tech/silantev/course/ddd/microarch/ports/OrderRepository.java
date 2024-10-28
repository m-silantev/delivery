package tech.silantev.course.ddd.microarch.ports;

import com.github.sviperll.result4j.Result;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;

import java.util.List;
import java.util.UUID;

public interface OrderRepository {

    Result<Order, Exception> add(Order order);

    Result<Order, Exception> update(Order order);

    Result<Order, Exception> getById(UUID id);

    Result<List<Order>, Exception> getAllCreated();

    Result<List<Order>, Exception> getAllAssigned();
}
