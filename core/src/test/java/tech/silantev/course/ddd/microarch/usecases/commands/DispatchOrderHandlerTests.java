package tech.silantev.course.ddd.microarch.usecases.commands;

import com.github.sviperll.result4j.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Courier;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;
import tech.silantev.course.ddd.microarch.domain.services.DispatchService;
import tech.silantev.course.ddd.microarch.ports.CourierRepository;
import tech.silantev.course.ddd.microarch.ports.OrderRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DispatchOrderHandlerTests {

    @Mock
    OrderRepository orderRepository;
    @Mock
    CourierRepository courierRepository;
    @Mock
    DispatchService dispatchService;

    @InjectMocks
    DispatchOrderHandler handler;

    @Test
    public void handlerShouldDispatchCreatedOrderToFreeCourierUsingDispatchService() {
        // given
        Courier mockCourier = mock(Courier.class);
        List<Courier> mockListOfFreeCouriers = List.of(mockCourier);
        when(courierRepository.getAllFree()).thenReturn(Result.success(mockListOfFreeCouriers));
        Order mockOrder = mock(Order.class);
        when(orderRepository.getAllCreated()).thenReturn(Result.success(List.of(mockOrder)));
        DispatchOrderCommand command = new DispatchOrderCommand();
        when(dispatchService.dispatch(mockOrder, mockListOfFreeCouriers)).thenReturn(Result.success(mockCourier));
        // when
        Result<Order, String> result = handler.handle(command);
        // then
        assertFalse(result.isError());
        assertEquals(mockOrder, result.discardError().get());
    }
}