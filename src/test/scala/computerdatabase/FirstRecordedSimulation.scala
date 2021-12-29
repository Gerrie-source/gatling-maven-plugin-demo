package computerdatabase

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class 	FirstRecordedSimulation extends Simulation {

	val httpProtocol = http
		.baseUrl("http://computer-database.gatling.io")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.5")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:95.0) Gecko/20100101 Firefox/95.0")


	val scn = scenario("FirstRecordedSimulation")
//		.exec(http("request_0")
//			.post(uri2)
//			.body(RawFileBody("computerdatabase/recordedsimulation/0000_request.dat"))
//			.resources(http("request_1")
//			.post(uri2)
//			.body(RawFileBody("computerdatabase/recordedsimulation/0001_request.dat"))))
//		.pause(27)
		.exec(http("request_2")
			.get("/computers"))
		.pause(7)
		.exec(http("request_3")
			.get("/computers"))
		.pause(46)
		// search
		.exec(http("request_4")
			.get("/computers?f=macbook"))
		.pause(11)
		.exec(http("request_5")
			.get("/computers/6"))
		.pause(30)
		// select computer
		.exec(http("request_6")
			.get("/computers/6"))
		.pause(14)
		.exec(http("request_7")
			.get("/computers"))
		.pause(14)
		// create computer
		.exec(http("request_8")
			.get("/computers/new"))
		.pause(34)
		.exec(http("request_9")
			.post("/computers")
			.formParam("name", "mycomputer")
			.formParam("introduced", "2010-02-03")
			.formParam("discontinued", "2020-03-01")
			.formParam("company", "3"))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}