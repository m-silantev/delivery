package tech.silantev.course.ddd.microarch.adapters.postgres;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainer extends PostgreSQLContainer<PostgresContainer> {

    private static final String IMAGE_VERSION = "postgres:15-alpine";
    private static PostgresContainer INSTANCE;

    private PostgresContainer() {
        super(IMAGE_VERSION);
    }

    public static PostgresContainer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PostgresContainer();
            INSTANCE.start();
        }
        return INSTANCE;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", getInstance().getJdbcUrl());
        System.setProperty("DB_USERNAME", getInstance().getUsername());
        System.setProperty("DB_PASSWORD", getInstance().getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}