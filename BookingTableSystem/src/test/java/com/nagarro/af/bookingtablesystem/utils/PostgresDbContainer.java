package com.nagarro.af.bookingtablesystem.utils;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresDbContainer extends PostgreSQLContainer<PostgresDbContainer> {
    private static final String IMAGE_VERSION = "postgres:latest";
    private static PostgresDbContainer container;

    private PostgresDbContainer() {
        super(IMAGE_VERSION);
    }

    public static PostgresDbContainer getInstance() {
        if (container == null) {
            container = new PostgresDbContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }
}
