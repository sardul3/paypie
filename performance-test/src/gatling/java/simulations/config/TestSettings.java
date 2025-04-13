package simulations.config;

import java.time.Duration;

public class TestSettings {
    public static final int AT_ONCE_USERS = 10;
    public static final int RAMP_USERS = 50;
    public static final Duration RAMP_DURATION = Duration.ofMinutes(2);
    public static final long MAX_RESPONSE_TIME_MS = 5000L;

    public static final int SPIKE_USERS = 100;
    public static final int CHAOS_USERS = 30;
    public static final Duration CHAOS_DURATION = Duration.ofMinutes(5);
    public static final int ERROR_USERS = 20;
    public static final Duration SOAK_DURATION = Duration.ofMinutes(10);

    // Stress test settings
    public static final double STRESS_USERS_PER_SEC = 5.0;
    public static final int STRESS_DURATION_MINUTES = 5;
} 