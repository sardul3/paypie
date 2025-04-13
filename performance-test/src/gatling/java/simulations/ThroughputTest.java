package simulations;


import io.gatling.javaapi.core.*;
import simulations.config.HttpConfig;
import simulations.config.RequestUtil;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ThroughputTest extends Simulation {

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
        setUp(scn.injectOpen(
                constantUsersPerSec(150).during(Duration.ofMinutes(1))
        ))
                .protocols(HttpConfig.httpProtocol)
                .assertions(global().responseTime().percentile3().lt(500));
    }
}

