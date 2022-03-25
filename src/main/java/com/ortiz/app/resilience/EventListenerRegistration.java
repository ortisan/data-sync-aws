package com.ortiz.app.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class EventListenerRegistration {

    private static Logger LOGGER = LoggerFactory.getLogger(EventListenerRegistration.class);

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @PostConstruct
    public void postConstruct() {
        circuitBreakerRegistry.getAllCircuitBreakers()
                .forEach(circuitBreaker -> circuitBreaker.getEventPublisher().onStateTransition(
                        (event) -> LOGGER.info(event.toString())
                ));

        circuitBreakerRegistry.getEventPublisher().onEntryAdded(
                (addedEvent) -> addedEvent.getAddedEntry().getEventPublisher().onStateTransition(
                        (event) -> LOGGER.info(event.toString())
                )
        );
    }
}
