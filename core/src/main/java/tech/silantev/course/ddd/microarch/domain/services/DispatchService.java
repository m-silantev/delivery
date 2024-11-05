package tech.silantev.course.ddd.microarch.domain.services;

import com.github.sviperll.result4j.Result;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Courier;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;

import java.util.List;

public interface DispatchService {
    /**
     * Выбирает наиболее подходящего курьера из списка и назначает заказ на него.
     * @return в случае успеха возвращает результат со ссылкой на выбранного курьера, иначе сообщение об ошибке.
     */
    Result<Courier, String> dispatch(Order order, List<Courier> couriers);
}
