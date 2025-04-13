package simulations.config;

import java.util.UUID;

public class RequestUtil {
    public static String uniqueGroupName() {
        return "team-lunch-" + UUID.randomUUID();
    }
}

