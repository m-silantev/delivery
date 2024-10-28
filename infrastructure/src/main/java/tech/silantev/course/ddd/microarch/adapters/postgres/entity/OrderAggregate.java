package tech.silantev.course.ddd.microarch.adapters.postgres.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

@Table("order_aggregate")
public class OrderAggregate {

    @Id
    public String id;
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    public LocationVO location;
    public Integer statusId;
    public String courierId;
}
