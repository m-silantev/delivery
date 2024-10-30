package tech.silantev.course.ddd.microarch.adapters.postgres.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("order_status")
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusEntity {

    @Id
    public Integer id;
    public String name;
}
