package io.github.sardul3.account.domain;

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
}
