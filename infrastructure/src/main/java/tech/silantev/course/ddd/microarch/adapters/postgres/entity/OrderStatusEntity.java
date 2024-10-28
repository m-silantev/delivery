package tech.silantev.course.ddd.microarch.adapters.postgres.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("order_status")
public class OrderStatusEntity {

    @Id
    public Integer id;
    public String name;
}
