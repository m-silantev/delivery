package tech.silantev.course.ddd.microarch.adapters.postgres;

import org.springframework.data.repository.CrudRepository;
import tech.silantev.course.ddd.microarch.adapters.postgres.entity.CourierAggregate;

import java.util.List;

public interface CourierAggregateJdbcRepository extends CrudRepository<CourierAggregate, String> {
    List<CourierAggregate> findAllByStatusId(int statusId);
}
