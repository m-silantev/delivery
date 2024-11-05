package tech.silantev.course.ddd.microarch.usecases.commands;

import com.github.sviperll.result4j.Result;
import org.junit.jupiter.api.Test;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CreateOrderHandlerTests {

    CreateOrderHandler handler = new CreateOrderHandler();

    @Test
    public void handlerShouldCreateOrderByCommand() {
        // given
        UUID basketId = UUID.randomUUID();
        String street = "anyString";
        CreateOrderCommand command = new CreateOrderCommand(basketId, street);
        // when
        Result<Order, String> result = handler.handle(command);
        // then
        assertFalse(result.isError());
        assertEquals(basketId, result.discardError().get().getId());
    }
}