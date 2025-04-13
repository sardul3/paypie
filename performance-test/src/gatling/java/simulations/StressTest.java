package simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import simulations.config.HttpConfig;
import simulations.config.TestSettings;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class StressTest extends Simulation {
    ScenarioBuilder scn = scenario("Stress Test")
        .exec(http("Create Expense Group")
            .post("/api/v1/expense-groups")
            .body(StringBody("""
                {
                    "name": "Stress Test Group ${userId}",
                    "description": "Created during stress test"
                }
                """))
            .check(status().is(201))
            .check(jsonPath("$.id").saveAs("groupId")))
        .pause(2)
        .exec(http("Add Expense")
            .post("/api/v1/expense-groups/${groupId}/expenses")
            .body(StringBody("""
                {
                    "amount": 100.00,
                    "description": "Stress test expense",
                    "date": "2024-03-20"
                }
                """))
            .check(status().is(201)))
        .pause(1)
        .exec(http("Get Group Details")
            .get("/api/v1/expense-groups/${groupId}")
            .check(status().is(200)));

    {
        setUp(
            scn.injectOpen(
                constantUsersPerSec(TestSettings.STRESS_USERS_PER_SEC)
                    .during(TestSettings.STRESS_DURATION_MINUTES * 60)
                    .randomized()
            )
        ).protocols(HttpConfig.httpProtocol)
        .assertions(
            global().successfulRequests().percent().gt(90.0),
            global().responseTime().percentile3().lt((int) TestSettings.MAX_RESPONSE_TIME_MS),
            global().failedRequests().count().lt((long) (TestSettings.STRESS_USERS_PER_SEC * TestSettings.STRESS_DURATION_MINUTES * 60 * 0.1))
        );
    }
} 