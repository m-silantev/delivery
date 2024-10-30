package tech.silantev.course.ddd.microarch.adapters.postgres.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;

@NoArgsConstructor
@AllArgsConstructor
public class LocationVO {
    @Column("location_x")
    public Integer x;
    @Column("location_y")
    public Integer y;
}
