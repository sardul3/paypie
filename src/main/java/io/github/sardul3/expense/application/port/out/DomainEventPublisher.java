package io.github.sardul3.expense.application.port.out;

import io.github.sardul3.expense.application.common.annotation.OutputPort;
import io.github.sardul3.expense.domain.event.DomainEvent;

/**
 * Output port: publishes domain events so that handlers can react (e.g. notify members).
 */
@OutputPort(
        description = "Publishes domain events for downstream handlers",
        role = OutputPort.Role.EVENT_PUBLISHER
)
public interface DomainEventPublisher {

    /**
     * Publishes a domain event. Implementations may be synchronous or asynchronous.
     *
     * @param event domain event (must not be null)
     */
    void publish(DomainEvent event);
}
