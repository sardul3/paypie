-- Expense groups table (idempotent for environments where table already exists)
CREATE TABLE IF NOT EXISTS expense_groups (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    activated BOOLEAN NOT NULL DEFAULT FALSE
);
