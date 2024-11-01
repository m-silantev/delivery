package tech.silantev.course.ddd.microarch.adapters.postgres.mapper;

import tech.silantev.course.ddd.microarch.adapters.postgres.entity.LocationVO;
import tech.silantev.course.ddd.microarch.adapters.postgres.entity.OrderAggregate;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.OrderStatus;
import tech.silantev.course.ddd.microarch.domain.sharedkernel.Location;

import java.util.Optional;
import java.util.UUID;

public interface OrderMapper {

    static Order entityToOrder(OrderAggregate entity) {
        return Order.builder()
                .id(UUID.fromString(entity.id))
                .location(Location.create(entity.location.x, entity.location.y))
                .status(OrderStatus.fromId(entity.statusId))
                .courierId(Optional.ofNullable(entity.courierId).map(UUID::fromString).orElse(null))
                .build();
    }

    static OrderAggregate orderToEntity(Order order) {
        OrderAggregate entity = new OrderAggregate();
        entity.id = order.getId().toString();
        entity.location = new LocationVO(order.getLocation().getX(), order.getLocation().getY());
        entity.statusId = order.getStatus().id();
        entity.courierId = order.getCourierId().map(UUID::toString).orElse(null);
        return entity;
    }
}
