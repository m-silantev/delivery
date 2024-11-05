package tech.silantev.course.ddd.microarch.usecases.queries;

import an.awesome.pipelinr.Command;
import com.github.sviperll.result4j.Result;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Courier;

import java.util.List;

public record GetAllBusyCouriersQuery(

) implements Command<Result<List<Courier>, String>> {

}
