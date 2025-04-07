package io.github.sardul3.expense.application.dto;

public class CreateExpenseGroupCommand {
    private final String name;
    private final String createdBy;

    public CreateExpenseGroupCommand(String name, String createdBy) {
        this.name = name;
        this.createdBy = createdBy;
    }

    public String getName() {
        return name;
    }

    public String getCreatedBy() {
        return createdBy;
    }
}
