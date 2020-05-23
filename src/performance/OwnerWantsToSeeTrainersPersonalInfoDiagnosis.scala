package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class OwnerWantsToSeeTrainersPersonalInfoDiagnosis extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "lv-LV,lv;q=0.9,en-US;q=0.8,en;q=0.7",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Accept-Language" -> "lv-LV,lv;q=0.9,en-US;q=0.8,en;q=0.7",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Language" -> "lv-LV,lv;q=0.9,en-US;q=0.8,en;q=0.7",
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_6 = Map(
		"A-IM" -> "x-bm,gzip",
		"Proxy-Connection" -> "keep-alive")

object Home {
val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(23)
}

object Login {
val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)))
		.pause(48)
}

object Logged {
val logged = exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "owner1")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "17e8667d-0294-4917-a285-937caf560bfd"))
		.pause(37)
}

object TrainersList {
val trainerlist = exec(http("TrainersList")
			.get("/trainers")
			.headers(headers_0))
		.pause(11)
}

object TrainerSuccessful {
val trainersuccessful = exec(http("TrainerSuccessful")
			.get("/trainers/1")
			.headers(headers_0))
		.pause(9)
}

object TrainerUnsuccessful {
val trainerunsuccessful = exec(http("TrainerUnsuccessful")
			.get("/trainers/-1")
			.headers(headers_0))
		.pause(14)
}

val OwnerSeesTrainersPersonalInfo = scenario("Successful").exec(
Home.home,
Login.login,
Logged.logged,
TrainersList.trainerlist,
TrainerSuccessful.trainersuccessful
)

val OwnerCantSeeTrainersPersonalInfo = scenario("Unsuccessful").exec(
Home.home,
Login.login,
Logged.logged,
TrainersList.trainerlist,
TrainerUnsuccessful.trainerunsuccessful
)

	setUp(OwnerSeesTrainersPersonalInfo.inject(rampUsers(1000) during (100 seconds)),
	      OwnerCantSeeTrainersPersonalInfo.inject(rampUsers(1000) during (100 seconds))

	).protocols(httpProtocol)
  .assertions(
    global.responseTime.max.lt(5000),
    global.responseTime.mean.lt(1000),
    global.successfulRequests.percent.gt(95)
  )
}