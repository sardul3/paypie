package io.github.sardul3.expense.adapter.out.event;

import io.github.sardul3.expense.application.port.out.DomainEventPublisher;
import io.github.sardul3.expense.domain.event.DomainEvent;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * DomainEventPublisher implementation that forwards each published event to a list of handlers.
 * Handlers are invoked in order; exceptions from one handler do not prevent others from being called.
 */
public class ForwardingDomainEventPublisher implements DomainEventPublisher {

    private final List<Consumer<DomainEvent>> handlers;

    /**
     * @param handlers list of handlers to invoke on each publish (each receives the event)
     */
    public ForwardingDomainEventPublisher(List<Consumer<DomainEvent>> handlers) {
        this.handlers = List.copyOf(Objects.requireNonNull(handlers, "handlers cannot be null"));
    }

    @Override
    public void publish(DomainEvent event) {
        Objects.requireNonNull(event, "event cannot be null");
        for (Consumer<DomainEvent> handler : handlers) {
            handler.accept(event);
        }
    }
}
