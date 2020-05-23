package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class VetWantsToSeePetsVisitsDiagnosis extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("lv-LV,lv;q=0.9,en-US;q=0.8,en;q=0.7")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")


object Home {
val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(14)
}	

object Login {
val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)))
		.pause(19)
}	

object Logged {
val logged = exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "vet1")
			.formParam("password", "v3t1")
			.formParam("_csrf", "43f8e440-db02-4bd6-bc97-b61c5e930e52"))
		.pause(14)
}

object FindOwners {
val findowners = exec(http("FindOwners")
			.get("/owners/find")
			.headers(headers_0))
		.pause(13)
}		

object OwnersInfoAndPetsVisits {
val ownersinfoandpetsvisits = exec(http("OwnersInfoAndPetsVisits")
			.get("/owners?lastName=Rodriquez")
			.headers(headers_0))
		.pause(19)
}		

object BrokenOwnersLink {
val brokenownerslink = exec(http("BrokenOwnersLink")
			.get("/owners/-1")
			.headers(headers_0))
		.pause(20)
}		
	

val VetSeesPetsVisits = scenario("Successful").exec(
Home.home,
Login.login,
Logged.logged,
FindOwners.findowners,
OwnersInfoAndPetsVisits.ownersinfoandpetsvisits
)

val VetCannotSeeBecauseOfBrokenLink = scenario("Unsuccessful").exec(
Home.home,
Login.login,
Logged.logged,
FindOwners.findowners,
BrokenOwnersLink.brokenownerslink
)

	setUp(VetSeesPetsVisits.inject(rampUsers(3000) during (100 seconds)),
	      VetCannotSeeBecauseOfBrokenLink.inject(rampUsers(3000) during (100 seconds))

	).protocols(httpProtocol)
  .assertions(
    global.responseTime.max.lt(5000),
    global.responseTime.mean.lt(1000),
    global.successfulRequests.percent.gt(95)
  )

}