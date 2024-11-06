package tech.silantev.course.ddd.microarch.usecases.queries;

import an.awesome.pipelinr.Command;
import com.github.sviperll.result4j.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Courier;
import tech.silantev.course.ddd.microarch.ports.CourierRepository;

import java.util.List;

@Component
public class GetAllCouriersHandler implements Command.Handler<GetAllCouriersQuery, Result<List<Courier>, String>> {

    private final CourierRepository courierRepository;

    @Autowired
    public GetAllCouriersHandler(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    @Override
    public Result<List<Courier>, String> handle(GetAllCouriersQuery command) {
        Result<List<Courier>, Exception> result = courierRepository.getAll();
        return result.mapError(Exception::getMessage);
    }
}
