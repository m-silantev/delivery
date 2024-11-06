package tech.silantev.course.ddd.microarch.adapters.postgres;

import com.github.sviperll.result4j.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tech.silantev.course.ddd.microarch.adapters.postgres.entity.CourierAggregate;
import tech.silantev.course.ddd.microarch.adapters.postgres.mapper.CourierMapper;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Courier;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.CourierStatus;
import tech.silantev.course.ddd.microarch.ports.CourierRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class PostgresCourierRepositoryImpl implements CourierRepository {

    private final CourierAggregateJdbcRepository repository;

    @Autowired
    public PostgresCourierRepositoryImpl(CourierAggregateJdbcRepository repository) {
        this.repository = repository;
    }

    @Override
    public Result<Courier, Exception> add(Courier courier) {
        try {
            CourierAggregate entity = CourierMapper.courierToEntity(courier);
            entity.create = true;
            CourierAggregate saved = repository.save(entity);
            return Result.success(CourierMapper.entityToCourier(saved));
        } catch (Exception e) {
            return Result.error(e);
        }
    }

    @Override
    public Result<Courier, Exception> update(Courier courier) {
        try {
            CourierAggregate entity = CourierMapper.courierToEntity(courier);
            CourierAggregate saved = repository.save(entity);
            return Result.success(CourierMapper.entityToCourier(saved));
        } catch (Exception e) {
            return Result.error(e);
        }
    }

    @Override
    public Result<Courier, Exception> getById(UUID id) {
        try {
            CourierAggregate entity = repository.findById(id.toString()).get();
            return Result.success(CourierMapper.entityToCourier(entity));
        } catch (Exception e) {
            return Result.error(e);
        }
    }

    @Override
    public Result<List<Courier>, Exception> getAll() {
        try {
            List<Courier> couriers = new ArrayList<>();
            for (CourierAggregate courier : repository.findAll()) {
                couriers.add(CourierMapper.entityToCourier(courier));
            }
            return Result.success(couriers);
        } catch (Exception e) {
            return Result.error(e);
        }
    }

    @Override
    public Result<List<Courier>, Exception> getAllFree() {
        try {
            List<Courier> allFree = repository.findAllByStatusId(CourierStatus.FREE.getId())
                    .stream().map(CourierMapper::entityToCourier).toList();
            return Result.success(allFree);
        } catch (Exception e) {
            return Result.error(e);
        }
    }

    @Override
    public Result<List<Courier>, Exception> getAllBusy() {
        try {
            List<Courier> allFree = repository.findAllByStatusId(CourierStatus.BUSY.getId())
                    .stream().map(CourierMapper::entityToCourier).toList();
            return Result.success(allFree);
        } catch (Exception e) {
            return Result.error(e);
        }
    }
}
