package tech.silantev.course.ddd.microarch.ports;

import com.github.sviperll.result4j.Result;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Courier;

import java.util.List;
import java.util.UUID;

public interface CourierRepository {

    Result<Courier, Exception> add(Courier courier);

    Result<Courier, Exception> update(Courier courier);

    Result<Courier, Exception> getById(UUID id);

    Result<List<Courier>, Exception> getAllFree();
}
