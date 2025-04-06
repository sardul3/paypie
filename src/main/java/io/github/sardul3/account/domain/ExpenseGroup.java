package io.github.sardul3.account.domain;

public class ExpenseGroup {
    private static final int MAX_GROUP_NAME_LENGTH_LIMIT = 50;

    private final String name;


    public ExpenseGroup(String name) {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if(name.length() > 50) {
            throw new IllegalArgumentException(
                    String.format("Name cannot be longer than {} characters",
                            MAX_GROUP_NAME_LENGTH_LIMIT));
        }
        this.name = name;
    }

    public static ExpenseGroup withName(String name) {
        return new ExpenseGroup(name);
    }

    public String getName() {
        return name;
    }
}
