package tech.silantev.course.ddd.microarch.adapters.postgres.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("courier_status")
@NoArgsConstructor
@AllArgsConstructor
public class CourierStatusEntity {

    @Id
    public Integer id;
    public String name;
}
