package tech.silantev.course.ddd.microarch.adapters.postgres.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

@Table("courier_aggregate")
@NoArgsConstructor
@AllArgsConstructor
public class CourierAggregate implements Persistable<String> {

    @Id
    public String id;
    public String name;
    public Integer transportId;
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    public LocationVO location;
    public Integer statusId;
    @Transient
    public boolean create;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return create;
    }
}
