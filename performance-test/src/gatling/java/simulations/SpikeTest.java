package simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import simulations.config.HttpConfig;
import simulations.config.TestSettings;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class SpikeTest extends Simulation {
    ScenarioBuilder scn = scenario("Spike Test")
        .exec(http("Get Expense Group")
            .get("/api/v1/expense-groups/${groupId}")
            .check(status().is(200)))
        .pause(1)
        .exec(http("Update Expense Group")
            .put("/api/v1/expense-groups/${groupId}")
            .body(StringBody("""
                {
                    "name": "Updated Group",
                    "description": "Updated during spike test"
                }
                """))
            .check(status().is(200)));

    {
        setUp(
            scn.injectOpen(
                nothingFor(5),
                atOnceUsers(TestSettings.SPIKE_USERS)
            )
        ).protocols(HttpConfig.httpProtocol)
        .assertions(
            global().successfulRequests().percent().gt(95.0),
            global().responseTime().max().lt((int) TestSettings.MAX_RESPONSE_TIME_MS)
        );
    }
} 