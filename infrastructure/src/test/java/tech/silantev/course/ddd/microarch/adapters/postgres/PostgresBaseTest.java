package tech.silantev.course.ddd.microarch.adapters.postgres;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Courier;
import tech.silantev.course.ddd.microarch.domain.courier.aggregate.Transport;
import tech.silantev.course.ddd.microarch.domain.order.aggregate.Order;
import tech.silantev.course.ddd.microarch.domain.sharedkernel.Location;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public abstract class PostgresBaseTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeAll
    public static void setup() {
        PostgresContainer.getInstance();
    }

    @AfterEach
    public void clear() {
        String sql = """
          TRUNCATE order_aggregate CASCADE;
          TRUNCATE courier_aggregate CASCADE;
        """;
        jdbcTemplate.execute(sql);
    }

    Courier createRandomCourier() {
        String name = "Courier #" + ThreadLocalRandom.current().nextInt();
        Transport transport = Transport.list().get(ThreadLocalRandom.current().nextInt(0, Transport.list().size()));
        Location location = Location.createRandom();
        return Courier.create(name, transport, location);
    }

    Order createRandomOrder() {
        UUID id = UUID.randomUUID();
        Location location = Location.createRandom();
        return Order.create(id, location);
    }
}
