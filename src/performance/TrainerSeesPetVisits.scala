package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class TrainerSeesPetVisits extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "en-US,en;q=0.9",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "en-US,en;q=0.9",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "en-US,en;q=0.9",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_5 = Map(
		"Proxy-Connection" -> "Keep-Alive",
		"User-Agent" -> "Microsoft-WNS/10.0")

  val uri2 = "http://cdn.content.prod.cms.msn.com/singletile/summary/alias/experiencebyname/today"
  
  object Home {
    val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(4)
  }
  
  object Login {
    val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
    ).pause(7)
    .exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "trainer1")
			.formParam("password", "tr41n3r")
			.formParam("_csrf", "${stoken}"))
		.pause(6)
  }
  
  object ListHomelessPets {
    val listHomelessPets = exec(http("ListHomelessPets")
			.get("/homeless-pets")
			.headers(headers_0))
		.pause(1)
  }
  
  object ShowHomelessPetVisits {
    val showHomelessPetVisits = exec(http("ShowHomelessPetVisits")
			.get("/homeless-pets/14")
			.headers(headers_0))
		.pause(40)
  }
  
  object ShowNoHomelessPetVisits {
    val showNoHomelessPetVisits = exec(http("ShowNoHomelessPetVisits")
			.get("/homeless-pets/-1")
			.headers(headers_0))
		.pause(27)
  }

	val trainerSeesPetVisitsPositiveScn = scenario("TrainerSeesPetVisitsPositive").exec(Home.home,
                                                                                        Login.login,
                                                                                        ListHomelessPets.listHomelessPets,
                                                                                        ShowHomelessPetVisits.showHomelessPetVisits)
  
  val trainerSeesPetVisitsNegativeScn = scenario("TrainerSeesPetVisitsNegative").exec(Home.home,
                                                                                        Login.login,
                                                                                        ListHomelessPets.listHomelessPets,
                                                                                        ShowNoHomelessPetVisits.showNoHomelessPetVisits)

	setUp(trainerSeesPetVisitsPositiveScn.inject(atOnceUsers(1)),
        trainerSeesPetVisitsNegativeScn.inject(atOnceUsers(1))
       ).protocols(httpProtocol)
}