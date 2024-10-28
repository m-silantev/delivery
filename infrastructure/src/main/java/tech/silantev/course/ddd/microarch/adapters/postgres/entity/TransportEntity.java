package tech.silantev.course.ddd.microarch.adapters.postgres.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("transport")
public class TransportEntity {

    @Id
    public Integer id;
    public String name;
    public Integer speed;
}
