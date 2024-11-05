package tech.silantev.course.ddd.microarch.usecases.queries;

import an.awesome.pipelinr.Command;
import com.github.sviperll.result4j.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;
import tech.silantev.course.ddd.microarch.ports.OrderRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Component
public class GetAllCreatedAndAssignedOrdersHandler implements Command.Handler<GetAllCreatedAndAssignedOrdersQuery, Result<List<Order>, String>> {

    private final OrderRepository orderRepository;

    @Autowired
    public GetAllCreatedAndAssignedOrdersHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Result<List<Order>, String> handle(GetAllCreatedAndAssignedOrdersQuery command) {
        Result<List<Order>, Exception> resultCreatedOrders = orderRepository.getAllCreated();
        if (resultCreatedOrders.isError()) {
            return resultCreatedOrders.mapError(Exception::getMessage);
        }
        Result<List<Order>, Exception> resultAssignedOrders = orderRepository.getAllAssigned();
        if (resultAssignedOrders.isError()) {
            return resultAssignedOrders.mapError(Exception::getMessage);
        }
        List<Order> combination = Stream.of(resultCreatedOrders.discardError().get(), resultCreatedOrders.discardError().get())
                .flatMap(Collection::stream)
                .distinct()
                .toList();
        return Result.success(combination);
    }
}
