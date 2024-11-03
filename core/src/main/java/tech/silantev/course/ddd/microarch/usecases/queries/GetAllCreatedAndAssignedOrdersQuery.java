package tech.silantev.course.ddd.microarch.usecases.queries;

import an.awesome.pipelinr.Command;
import com.github.sviperll.result4j.Result;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;

import java.util.List;

public record GetAllCreatedAndAssignedOrdersQuery(

) implements Command<Result<List<Order>, String>> {

}
