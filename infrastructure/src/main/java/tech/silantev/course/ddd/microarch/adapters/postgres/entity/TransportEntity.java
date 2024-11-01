package tech.silantev.course.ddd.microarch.adapters.postgres.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("transport")
@NoArgsConstructor
@AllArgsConstructor
public class TransportEntity {

    @Id
    public Integer id;
    public String name;
    public Integer speed;
}
