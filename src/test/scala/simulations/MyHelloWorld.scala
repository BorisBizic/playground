package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._


class MyHelloWorld extends Simulation{

  // 1 Http Conf
  val httpConf: HttpProtocolBuilder = http.baseUrl("https://dev.api.hrm.htec.co.rs/api/")
    .header("Accept", "application/json")
    .authorizationHeader("Bearer ew0KICAiYWxnIjogIkhTMjU2IiwNCiAgInR5cCI6ICJKV1QiDQp9.ew0KICAic3ViIjogIjM0NyIsDQogICJVc2VySWQiOiAiMzQ1IiwNCiAgImVtYWlsIjogImJvcmlzLmJhamFAaHRlY2dyb3VwLmNvbSIsDQogICJyb2xlIjogWw0KICAgICJNZW50b3IiLA0KICAgICJIUiIsDQogICAgIk1hbmFnZW1lbnQiDQogIF0sDQogICJSb2xlSWQiOiBbDQogICAgIjE0IiwNCiAgICAiNCIsDQogICAgIjUiDQogIF0sDQogICJ0b2tlbl91c2FnZSI6ICJhY2Nlc3NfdG9rZW4iLA0KICAianRpIjogIjllYjNiMjgzLTcyMzctNDNkNi1hODQ5LTRmY2E5YmI5ZmI2MiIsDQogICJzY29wZSI6IFsNCiAgICAicHJvZmlsZSIsDQogICAgIm9mZmxpbmVfYWNjZXNzIg0KICBdLA0KICAiYXVkIjogImh0dHA6Ly9kZXYuYXBpLmhybS5odGVjLmNvLnJzOjgwIiwNCiAgIm5iZiI6IDE1ODA3MzU3MTksDQogICJleHAiOiAxNTg0MzM1NzE5LA0KICAiaWF0IjogMTU4MDczNTcxOSwNCiAgImlzcyI6ICJodHRwczovL2Rldi5hcGkuaHJtLmh0ZWMuY28ucnMvIg0KfQ.MXVdNwJsIJcoSzds3HcnBXm3f_HvQY760Ixulnmij70")


  def getAllProfile(): ChainBuilder = {
    exec(
      http("Get My Profile")
        .get("v2/People")
        .check(status.is(200)))
  }

  // 2 Scenario Definition
  val scn1: ScenarioBuilder = scenario("My Hello Test World")
    .exec(getAllProfile())
      .pause(5 seconds)
/*    .check(bodyString.saveAs("responseBody")))
  .exec { session => println(session("responseBody").as[String]); session }*/

  val scn2: ScenarioBuilder = scenario("My Hello Test World 2")
  .exec(getAllProfile())
    .pause(5 seconds)



  // 3 Load Scenario
  setUp(
    scn1.inject(
      nothingFor(5 seconds),
      atOnceUsers(5),
      rampUsers(10) during (10 seconds)
    ).protocols(httpConf.inferHtmlResources()),
    scn2.inject(
      atOnceUsers(50)
    ).protocols(httpConf)
  )
}
