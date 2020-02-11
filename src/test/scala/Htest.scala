import java.io.InputStream
import java.util.Properties

import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration.DurationInt

class Htest extends Simulation {

  val inputStream: InputStream = getClass.getClassLoader.getResourceAsStream("config.properties")
  val properties: Properties = new Properties()
  properties.load(inputStream)
  var token: String = "Bearer " + properties.getProperty("TOKEN")


  val httpConf: HttpProtocolBuilder = http
    .baseUrl("http://localhost:5000")
    .header("Accept", "application/json")
    .authorizationHeader(token)

  def getGroups: ChainBuilder = {
    exec(
      http("Get Project's groups")
        .get("/api/groups?project_id=1")
        .check(status.in(200, 304))
    )
  }

  def postTestCase: ChainBuilder = {
    exec(
      http("Post Test Cases")
        .post("/api/testcases?project_id=1&page=1&page_size=15")
        .body(RawFileBody("bodies/postBody.json")).asJson
        .check(status.is(200))
    )
  }

  val scn: ScenarioBuilder = scenario("Htest scenario")
    .forever() {
      exec(getGroups)
        .pause(2, 5 seconds)
        .exec(postTestCase)
        .pause(2 seconds)
    }

  setUp(
    scn.inject(
      nothingFor(5 seconds),
      rampUsers(5) during (30 seconds))
  )
    .assertions(
      global.responseTime.max.lt(500),
      forAll.failedRequests.count.lt(5),
      details("Bad Request").successfulRequests.percent.gt(90)
    )
    .protocols(httpConf)
    .maxDuration(30 seconds)

  /*setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)*/
}
