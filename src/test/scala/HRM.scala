import io.gatling.http.Predef._
import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration.DurationInt

class HRM extends Simulation {

  val httpConf: HttpProtocolBuilder = http
    .baseUrl("https://dev.api.hrm.htec.co.rs")
    .header("Accept", "application/json")
    .authorizationHeader("Bearer ew0KICAiYWxnIjogIkhTMjU2IiwNCiAgInR5cCI6ICJKV1QiDQp9.ew0KICAic3ViIjogIjM0NyIsDQogICJVc2VySWQiOiAiMzQ1IiwNCiAgImVtYWlsIjogImJvcmlzLmJhamFAaHRlY2dyb3VwLmNvbSIsDQogICJyb2xlIjogWw0KICAgICJNZW50b3IiLA0KICAgICJIUiIsDQogICAgIk1hbmFnZW1lbnQiDQogIF0sDQogICJSb2xlSWQiOiBbDQogICAgIjE0IiwNCiAgICAiNCIsDQogICAgIjUiDQogIF0sDQogICJ0b2tlbl91c2FnZSI6ICJhY2Nlc3NfdG9rZW4iLA0KICAianRpIjogIjllYjNiMjgzLTcyMzctNDNkNi1hODQ5LTRmY2E5YmI5ZmI2MiIsDQogICJzY29wZSI6IFsNCiAgICAicHJvZmlsZSIsDQogICAgIm9mZmxpbmVfYWNjZXNzIg0KICBdLA0KICAiYXVkIjogImh0dHA6Ly9kZXYuYXBpLmhybS5odGVjLmNvLnJzOjgwIiwNCiAgIm5iZiI6IDE1ODA3MzU3MTksDQogICJleHAiOiAxNTg0MzM1NzE5LA0KICAiaWF0IjogMTU4MDczNTcxOSwNCiAgImlzcyI6ICJodHRwczovL2Rldi5hcGkuaHJtLmh0ZWMuY28ucnMvIg0KfQ.MXVdNwJsIJcoSzds3HcnBXm3f_HvQY760Ixulnmij70")

  def getAllProfiles: ChainBuilder = {
    exec(
      http("Get All Profiles")
        .get("/api/v2/People")
        .check(status.is(200))
    )
  }

  def getMyProfile: ChainBuilder= {
  exec(
    http("Get My Profile")
      .get("/api/v2/People/345")
      .check(status.is(200))
  )
  }

  val scn: ScenarioBuilder = scenario("HRM Profile")
    .forever() {
      exec(getAllProfiles)
        .pause(2 seconds)
        .exec(getMyProfile)
        .pause(2 seconds)
    }

  setUp(
    scn.inject(
      nothingFor(5 seconds),
      rampUsers(5) during (30 seconds))
  )
    .protocols(httpConf)
    .maxDuration(60 seconds)

}
