package com.example.demo;

import brave.Tracing;
import brave.baggage.BaggageField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class DemoController {
    private Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    public BaggageField customerOrderIdBaggage;

    @Autowired
    public Tracing tracing;

    @Autowired
    public Service service;

    @GetMapping("/non-transactional")
    public Mono<Void> nonTransactionalService() {
        customerOrderIdBaggage.updateValue(tracing.tracer().currentSpan().context(), "external_id_value");
        logger.info("Before Mono.defer on non transactional controller");

        return Mono.defer(() -> {
                    logger.info("on Mono.defer");
                    return Mono.empty();
                })
                .then(service.transactional());
    }

    @Transactional
    @GetMapping("/transactional")
    public Mono<Void> empty() {
        customerOrderIdBaggage.updateValue(tracing.tracer().currentSpan().context(), "external_id_value");
        logger.info("Before Mono.defer on the transactional controller");

        return Mono.defer(() -> {
                    logger.info("on Mono.defer");
                    return Mono.empty();
                })
                .then(service.nonTransactional());
    }
}
