package com.jasonvillar.works.register.configs_for_tests.repositories;

import org.testcontainers.containers.PostgreSQLContainer;

public class Postgres15_2TC extends PostgreSQLContainer<Postgres15_2TC> {
    private static final Postgres15_2TC TC = new Postgres15_2TC();

    private Postgres15_2TC() {
        super("postgres:15.2-alpine");
    }

    public static Postgres15_2TC getInstance() {
        return TC;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", TC.getJdbcUrl());
        System.setProperty("DB_USERNAME", TC.getUsername());
        System.setProperty("DB_PASSWORD", TC.getPassword());
    }

    @Override
    public void stop() {
    }
}
