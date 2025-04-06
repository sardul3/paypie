package io.github.sardul3.account.domain;

import java.util.Objects;

@ValueObject(
        description = "Represents a valid and normalized group name for expense tracking",
        boundedContext = "expense-management"
)
public class GroupName {
    private static final int MAX_GROUP_NAME_LENGTH_LIMIT = 50;

    private final String name;

    public GroupName(String name) {
        if(name == null ) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        String trimmedName = name.trim();
        if(trimmedName.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if(trimmedName.length() > 50) {
            throw new IllegalArgumentException(
                    String.format("Name cannot be longer than {} characters",
                            MAX_GROUP_NAME_LENGTH_LIMIT));
        }
        this.name = trimmedName;
    }

    public static GroupName withName(String name) {
        return new GroupName(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupName groupName = (GroupName) o;
        return Objects.equals(name, groupName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "GroupName{" +
                "name='" + name + '\'' +
                '}';
    }
}
