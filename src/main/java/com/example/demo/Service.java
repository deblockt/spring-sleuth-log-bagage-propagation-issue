package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Component
public class Service {
    private Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    public DatabaseClient client;

    @Transactional
    public Mono<Void> transactional() {
        return client.sql("select 1")
                .map(row -> row.get(0))
                .first()
                .doOnNext(row -> logger.info("doOnNext.transactionalService {}", row))
                .then();
    }

    public Mono<Void> nonTransactional() {
        return client.sql("select 1")
                .map(row -> row.get(0))
                .first()
                .doOnNext(row -> logger.info("doOnNext.nontransactionalService {}", row))
                .then();
    }
}
