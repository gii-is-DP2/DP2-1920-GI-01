package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class UnregisteredUserSeesTrainers extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.9")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map("Proxy-Connection" -> "keep-alive")
  
  object Home {
    val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(6)
  }
  
  object ListTrainers {
    val listTrainers = exec(http("ListTrainers")
			.get("/trainers")
			.headers(headers_0))
		.pause(5)
  }

	val unregisteredUserSeesTrainersScn = scenario("UnregisteredUserSeesTrainers").exec(Home.home,
                                                                                      ListTrainers.listTrainers)

	setUp(unregisteredUserSeesTrainersScn.inject(atOnceUsers(1))).protocols(httpProtocol)
}