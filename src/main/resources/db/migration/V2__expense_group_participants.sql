-- Participants per expense group (group_id, participant_id, email, balance)
CREATE TABLE expense_group_participants (
    group_id UUID NOT NULL REFERENCES expense_groups(id) ON DELETE CASCADE,
    participant_id UUID NOT NULL,
    email VARCHAR(255) NOT NULL,
    balance_amount DECIMAL(19, 2) NOT NULL DEFAULT 0,
    PRIMARY KEY (group_id, participant_id)
);

CREATE INDEX idx_expense_group_participants_group_id ON expense_group_participants(group_id);
