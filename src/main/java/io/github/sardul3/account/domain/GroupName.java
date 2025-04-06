package io.github.sardul3.account.domain;

import java.util.Objects;

public class GroupName {
    private static final int MAX_GROUP_NAME_LENGTH_LIMIT = 50;

    private final String name;

    public GroupName(String name) {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if(name.length() > 50) {
            throw new IllegalArgumentException(
                    String.format("Name cannot be longer than {} characters",
                            MAX_GROUP_NAME_LENGTH_LIMIT));
        }
        this.name = name.trim();
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
}
