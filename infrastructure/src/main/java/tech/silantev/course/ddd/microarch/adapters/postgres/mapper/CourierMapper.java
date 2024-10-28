package tech.silantev.course.ddd.microarch.adapters.postgres.mapper;

import tech.silantev.course.ddd.microarch.adapters.postgres.entity.CourierAggregate;
import tech.silantev.course.ddd.microarch.adapters.postgres.entity.LocationVO;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Courier;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.CourierStatus;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Transport;
import tech.silantev.course.ddd.microarch.domain.sharedkernel.Location;

import java.util.UUID;

public interface CourierMapper {

    static Courier entityToCourier(CourierAggregate entity) {
        return Courier.builder()
                .id(UUID.fromString(entity.id))
                .name(entity.name)
                .transport(Transport.fromId(entity.transportId))
                .location(Location.create(entity.location.x, entity.location.y))
                .status(CourierStatus.fromId(entity.statusId))
                .build();
    }

    static CourierAggregate courierToEntity(Courier courier) {
        CourierAggregate entity = new CourierAggregate();
        entity.id = courier.getId().toString();
        entity.name = courier.getName();
        entity.transportId = courier.getTransport().getId();
        entity.location = new LocationVO(courier.getLocation().getX(), courier.getLocation().getY());
        entity.statusId = courier.getTransport().getId();
        return entity;
    }
}