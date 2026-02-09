package io.github.sardul3.expense.adapter.out.event;

import io.github.sardul3.expense.domain.event.DomainEvent;
import io.github.sardul3.expense.domain.event.ParticipantAddedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ForwardingDomainEventPublisher")
class ForwardingDomainEventPublisherTest {

    private List<DomainEvent> receivedEvents;
    private ForwardingDomainEventPublisher publisher;

    @BeforeEach
    void setUp() {
        receivedEvents = new ArrayList<>();
        publisher = new ForwardingDomainEventPublisher(List.of(receivedEvents::add));
    }

    @Test
    @DisplayName("should invoke handler when event is published")
    void shouldInvokeHandlerWhenEventPublished() {
        ParticipantAddedEvent event = new ParticipantAddedEvent(
                UUID.randomUUID(), UUID.randomUUID(), "a@b.com");

        publisher.publish(event);

        assertThat(receivedEvents).containsExactly(event);
    }

    @Test
    @DisplayName("should invoke all handlers when multiple registered")
    void shouldInvokeAllHandlersWhenMultipleRegistered() {
        List<DomainEvent> secondReceived = new ArrayList<>();
        publisher = new ForwardingDomainEventPublisher(List.of(receivedEvents::add, secondReceived::add));
        ParticipantAddedEvent event = new ParticipantAddedEvent(
                UUID.randomUUID(), UUID.randomUUID(), "a@b.com");

        publisher.publish(event);

        assertThat(receivedEvents).containsExactly(event);
        assertThat(secondReceived).containsExactly(event);
    }
}
