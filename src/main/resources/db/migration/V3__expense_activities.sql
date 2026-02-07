-- Expense activities per group (for history and aggregate reconstitution)
CREATE TABLE expense_activities (
    id UUID PRIMARY KEY,
    group_id UUID NOT NULL REFERENCES expense_groups(id) ON DELETE CASCADE,
    description VARCHAR(50) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    paid_by_participant_id UUID NOT NULL,
    split_evenly BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_expense_activities_group_id ON expense_activities(group_id);
