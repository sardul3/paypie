package io.github.sardul3.expense.expense.persistence;

import io.github.sardul3.expense.adapter.out.persistence.postgres.ExpenseGroupJpaRepository;
import io.github.sardul3.expense.adapter.out.persistence.postgres.PostgresExpenseGroupRepository;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import(PostgresExpenseGroupRepository.class)
@DisplayName("ExpenseGroup | Postgres | Persistence Behavior")
public class PostGresExpenseGroupRepositoryTest {

    @Autowired
    private ExpenseGroupJpaRepository jpaRepository;

    private ExpenseGroupRepository repository;

    @BeforeEach
    void setUp() {
        repository = new PostgresExpenseGroupRepository(jpaRepository);
    }
    @Test
    void shouldSaveAndRetrieveAnExpenseGroup() {
        ExpenseGroup group = ExpenseGroup.from(GroupName.withName("demo"), Participant.withEmail("a@b.com"));

        ExpenseGroup saved = repository.save(group);

        assertThat(saved).isNotNull();
        assertThat(repository.existsByName(GroupName.withName("demo"))).isTrue();
    }
}
