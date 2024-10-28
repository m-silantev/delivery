package tech.silantev.course.ddd.microarch.adapters.postgres.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

@Table("courier_aggregate")
public class CourierAggregate {

    @Id
    public String id;
    public String name;
    public Integer transportId;
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    public LocationVO location;
    public Integer statusId;
}
