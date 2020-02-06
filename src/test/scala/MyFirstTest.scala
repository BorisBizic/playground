import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class MyFirstTest extends Simulation {

  // 1 Http Conf
  val httpConf: HttpProtocolBuilder = http.baseUrl("http://localhost:8080/app/")
    .header("Accept", "application/json")
    .proxy(Proxy("localhost", 8888))


  // 2 Scenario Definition
  val scn: ScenarioBuilder = scenario("My First Test")
    .exec(http("Get All Games")
      .get("videogames"))

  // 3 Load Scenario
  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)

}
