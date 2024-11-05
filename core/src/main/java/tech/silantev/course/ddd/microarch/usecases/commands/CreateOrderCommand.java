package tech.silantev.course.ddd.microarch.usecases.commands;

import an.awesome.pipelinr.Command;
import com.github.sviperll.result4j.Result;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;

import java.util.UUID;

public record CreateOrderCommand(
        UUID basketId,
        String street
) implements Command<Result<Order, String>> {

}
