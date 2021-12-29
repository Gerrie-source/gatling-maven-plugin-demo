package computerdatabase

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._


class RecordedSimulation extends Simulation {

  val httpConfig = http
    .baseUrl("https://computer-database.gatling.io")
    .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-GB,en;q=0.9,en-US;q=0.8,af;q=0.7")
    .upgradeInsecureRequestsHeader("1")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36")

  object Search {

    val searchFeeder = csv("data/search.csv").random

    val search = exec(http("Load_HomePage")
      .get("/computers"))
      .pause(2)
      .feed(searchFeeder)
      .exec(http("Search_Computer_${searchCriterion}")
        .get("/computers?f=${searchCriterion}")
        .check(css("a:contains('${searchComputerName}')", "href").saveAs("computerURL")))
      .pause(2)
      .exec(http("Select Computer_${searchComputerName}")
        .get("${computerURL}"))
      .pause(2)
  }

  object Browse {
    val browse =
      repeat(5, "i") {
        exec(http("Browse_Page_${i}")
          .get("/computers?p=${i}"))
          .pause(2)
      }
  }

  object Create {

    val computerFeeder = csv("data/computers.csv").circular

    val create = exec(http("Load_Create_Computer_Page")
      .get("/computers/new"))
      .pause(2)
      .feed(computerFeeder)
      .exec(http("Create_Computer_${computerName}")
        .post("/computers")
        .formParam("name", "${computerName}")
        .formParam("introduced", "${introduced}")
        .formParam("discontinued", "${discontinued}")
        .formParam("company", "${companyId}")
      .check(status.is(200)))
  }

  val admins = scenario("Admins").exec(Search.search, Browse.browse, Create.create)

  val users = scenario("Users").exec(Search.search, Browse.browse)

  setUp(admins.inject(atOnceUsers(5)),

    users.inject(
      nothingFor(5),
      atOnceUsers(1),
      rampUsers(5) during(10),
      constantUsersPerSec(20) during(20)
    ))

    .protocols(httpConfig)
}