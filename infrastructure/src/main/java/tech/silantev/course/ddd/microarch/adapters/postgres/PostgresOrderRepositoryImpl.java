package tech.silantev.course.ddd.microarch.adapters.postgres;

import com.github.sviperll.result4j.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tech.silantev.course.ddd.microarch.adapters.postgres.entity.OrderAggregate;
import tech.silantev.course.ddd.microarch.adapters.postgres.mapper.OrderMapper;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.OrderStatus;
import tech.silantev.course.ddd.microarch.ports.OrderRepository;

import java.util.List;
import java.util.UUID;

@Repository
public class PostgresOrderRepositoryImpl implements OrderRepository {

    private final OrderAggregateJdbcRepository repository;

    @Autowired
    public PostgresOrderRepositoryImpl(OrderAggregateJdbcRepository repository) {
        this.repository = repository;
    }

    @Override
    public Result<Order, Exception> add(Order order) {
        try {
            OrderAggregate entity = OrderMapper.orderToEntity(order);
            entity.create = true;
            OrderAggregate saved = repository.save(entity);
            return Result.success(OrderMapper.entityToOrder(saved));
        } catch (Exception e) {
            return Result.error(e);
        }
    }

    @Override
    public Result<Order, Exception> update(Order order) {
        try {
            OrderAggregate entity = OrderMapper.orderToEntity(order);
            OrderAggregate saved = repository.save(entity);
            return Result.success(OrderMapper.entityToOrder(saved));
        } catch (Exception e) {
            return Result.error(e);
        }
    }

    @Override
    public Result<Order, Exception> getById(UUID id) {
        try {
            OrderAggregate entity = repository.findById(id.toString()).get();
            return Result.success(OrderMapper.entityToOrder(entity));
        } catch (Exception e) {
            return Result.error(e);
        }
    }

    @Override
    public Result<List<Order>, Exception> getAllCreated() {
        try {
            List<Order> allCreated = repository.findAllByStatusId(OrderStatus.CREATED.id())
                    .stream().map(OrderMapper::entityToOrder).toList();
            return Result.success(allCreated);
        } catch (Exception e) {
            return Result.error(e);
        }
    }

    @Override
    public Result<List<Order>, Exception> getAllAssigned() {
        try {
            List<Order> allAssigned = repository.findAllByStatusId(OrderStatus.ASSIGNED.id())
                    .stream().map(OrderMapper::entityToOrder).toList();
            return Result.success(allAssigned);
        } catch (Exception e) {
            return Result.error(e);
        }
    }
}
