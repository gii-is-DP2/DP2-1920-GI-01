package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class OwnerWantsToSeePetsRehab extends Simulation {

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
		.pause(10)
}

object Login {
val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)))
		.pause(24)
}
	
object Logged {
val logged = exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "owner1")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "2422171e-c8c9-42bf-90e2-902deb77b176"))
		.pause(8)
}

object FindOwner {
val findowner = exec(http("FindOwner")
			.get("/owners/find")
			.headers(headers_0))
		.pause(19)
}

object OwnerInfoAndRehabs {
val ownerinfoandrehabs = exec(http("OwnerInfoAndRehabs")
			.get("/owners?lastName=Rodriquez")
			.headers(headers_0))
		.pause(27)
}
		
object UnsuccessfulRehabSeeing {
val unsuccessfulrehabseeing = exec(http("UnsuccessfulRehabSeeing")
			.get("/owners/-3")
			.headers(headers_0))
		.pause(83)
}

		
val OwnerSeesPetsRehabsSuccessful = scenario("Successful").exec(
Home.home,
Login.login,
Logged.logged,
FindOwner.findowner,
OwnerInfoAndRehabs.ownerinfoandrehabs
)

val OwnerOpensBrokenLink = scenario("Unsuccessful").exec(
Home.home,
Login.login,
Logged.logged,
FindOwner.findowner,
UnsuccessfulRehabSeeing.unsuccessfulrehabseeing
)

setUp(OwnerSeesPetsRehabsSuccessful.inject(atOnceUsers(1)),
	     OwnerOpensBrokenLink.inject(atOnceUsers(1))
     ).protocols(httpProtocol)

	
}