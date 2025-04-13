package simulations;

import io.gatling.javaapi.core.*;
import simulations.config.HttpConfig;
import simulations.config.RequestUtil;
import simulations.config.TestSettings;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class LoadTest extends Simulation {
    ScenarioBuilder scn = scenario("Load Test")
            .exec(session -> session.set("groupName", RequestUtil.uniqueGroupName()))
            .exec(http("Create Expense Group")
                    .post("/api/v1/expense/groups")
                    .body(StringBody(session -> """
                {
                  "name": "%s",
                  "createdBy": "load@test.com"
                }
            """.formatted(session.getString("groupName")))).asJson()
                    .check(status().is(201))
            );

    {
        setUp(
            scn.injectOpen(
                atOnceUsers(TestSettings.AT_ONCE_USERS),
                rampUsers(TestSettings.RAMP_USERS).during(TestSettings.RAMP_DURATION)
            )
        ).protocols(HttpConfig.httpProtocol)
        .assertions(
            global().successfulRequests().percent().gt(98.0),
            global().responseTime().max().lt((int) TestSettings.MAX_RESPONSE_TIME_MS)
        );
    }
} 