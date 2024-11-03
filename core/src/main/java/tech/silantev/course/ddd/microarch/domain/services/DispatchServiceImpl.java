package tech.silantev.course.ddd.microarch.domain.services;

import com.github.sviperll.result4j.Result;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Courier;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.CourierStatus;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.OrderStatus;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DispatchServiceImpl implements DispatchService {

    @Override
    public Result<Courier, String> dispatch(Order order, List<Courier> couriers) {
        if (order.getStatus() != OrderStatus.CREATED) {
            return Result.error("Can't dispatch order. It must be in CREATED status to be dispatched, but it is in " + order.getStatus());
        }
        Optional<Courier> fastestCourier = couriers.stream().filter(courier -> courier.getStatus() == CourierStatus.FREE)
                .min(Comparator.comparingDouble(courier -> courier.calculateTimeTo(order.getLocation())));
        if (fastestCourier.isEmpty()) {
            return Result.error("Can't dispatch order. There are no free couriers");
        }
        order.assignCourier(fastestCourier.get());
        return Result.success(fastestCourier.get());
    }
}
