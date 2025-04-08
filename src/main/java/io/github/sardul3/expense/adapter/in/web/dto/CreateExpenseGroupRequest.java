package io.github.sardul3.expense.adapter.in.web.dto;

public record CreateExpenseGroupRequest(String name, String createdBy) {

    public CreateExpenseGroupRequest(String name, String createdBy) {
        this.name = name;
        this.createdBy = createdBy;
    }
}
