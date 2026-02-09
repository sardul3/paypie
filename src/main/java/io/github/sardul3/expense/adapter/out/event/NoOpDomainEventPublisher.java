package io.github.sardul3.expense.adapter.out.event;

import io.github.sardul3.expense.application.port.out.DomainEventPublisher;
import io.github.sardul3.expense.domain.event.DomainEvent;
import org.springframework.stereotype.Component;

/**
 * No-op implementation of DomainEventPublisher. Use for tests or when no handlers are configured.
 * Replace with a real implementation (e.g. Spring ApplicationEventPublisher or message broker) when needed.
 */
@Component
public class NoOpDomainEventPublisher implements DomainEventPublisher {

    @Override
    public void publish(DomainEvent event) {
        // no-op
    }
}
