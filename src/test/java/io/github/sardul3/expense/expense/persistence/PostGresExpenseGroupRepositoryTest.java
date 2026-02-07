package io.github.sardul3.expense.expense.persistence;

import io.github.sardul3.expense.adapter.out.persistence.postgres.repository.ExpenseGroupJpaRepository;
import io.github.sardul3.expense.adapter.out.persistence.postgres.repository.ParticipantJpaRepository;
import io.github.sardul3.expense.adapter.out.persistence.postgres.repository.PostgresExpenseGroupRepository;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import io.github.sardul3.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Tag("integration")
@Import(PostgresExpenseGroupRepository.class)
@DisplayName("ExpenseGroup | Postgres | Persistence Behavior")
public class PostGresExpenseGroupRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private ExpenseGroupJpaRepository jpaRepository;

    @Autowired
    private ParticipantJpaRepository participantJpaRepository;

    private ExpenseGroupRepository repository;

    @BeforeEach
    void setUp() {
        repository = new PostgresExpenseGroupRepository(jpaRepository, participantJpaRepository);
    }
    @Test
    void shouldSaveAndRetrieveAnExpenseGroup() {
        ExpenseGroup group = ExpenseGroup.from(GroupName.withName("demo"), Participant.withEmail("a@b.com"));

        ExpenseGroup saved = repository.save(group);

        assertThat(saved).isNotNull();
        assertThat(repository.existsByName(GroupName.withName("demo"))).isTrue();
    }

    @Test
    void shouldReturnFalseWhenGroupNameDoesNotExist() {
        boolean exists = repository.existsByName(GroupName.withName("non-existent"));
        assertThat(exists).isFalse();
    }
}
