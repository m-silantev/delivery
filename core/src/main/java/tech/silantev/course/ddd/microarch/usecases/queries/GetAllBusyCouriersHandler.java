package tech.silantev.course.ddd.microarch.usecases.queries;

import an.awesome.pipelinr.Command;
import com.github.sviperll.result4j.Result;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Courier;
import tech.silantev.course.ddd.microarch.ports.CourierRepository;

import java.util.List;

public class GetAllBusyCouriersHandler implements Command.Handler<GetAllBusyCouriersQuery, Result<List<Courier>, String>> {

    private final CourierRepository courierRepository;

    public GetAllBusyCouriersHandler(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    @Override
    public Result<List<Courier>, String> handle(GetAllBusyCouriersQuery command) {
        Result<List<Courier>, Exception> result = courierRepository.getAllBusy();
        return result.mapError(Exception::getMessage);
    }
}
