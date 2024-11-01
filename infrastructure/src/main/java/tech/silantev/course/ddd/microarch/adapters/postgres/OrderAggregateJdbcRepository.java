package tech.silantev.course.ddd.microarch.adapters.postgres;

import org.springframework.data.repository.CrudRepository;
import tech.silantev.course.ddd.microarch.adapters.postgres.entity.OrderAggregate;

import java.util.List;

public interface OrderAggregateJdbcRepository extends CrudRepository<OrderAggregate, String> {
    List<OrderAggregate> findAllByStatusId(int statusId);
}
