package myfirstscalatest;

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class FirstScalaTests extends Simulation {
    val baseUrl = "http://www.swtestacademy.com"
    val httpProtocol =
        http
          .baseUrl(baseUrl)
          .userAgentHeader("Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.5) Gecko/20091102 Firefox/3.5.5 (.NET CLR 3.5.30729)")

    val header_0 = Map("Accept" -> "text/html", "Accept-Language" -> "en-US")
    val header_1 = Map("Content-Type" -> "application/json", "Accept-Charset" -> "utf-8")

    val scn1 = scenario("Scenario 1")
      .exec(http("Post comment").post("/comment")
        //.header(header_0).check(status.is(200))
      )

//    val scn2 = scenario("Scenario 2")
//      .exec(http("Get Selenium articles")
//        .get("/category/selenium").header(header_1)
//        .check(status.not(404), status.not(500))
//      )
      {
          setUp(scn1.inject(atOnceUsers(1)).protocols(httpProtocol))
            //scn2.inject(atOnceUsers(2)).protocols(httpProtocol))
      }
}