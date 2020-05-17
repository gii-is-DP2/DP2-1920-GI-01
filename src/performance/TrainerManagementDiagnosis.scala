package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class TrainerManagementDiagnosis extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.9")
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
		.pause(4)
  }
  
  object Login {
    val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
    ).pause(8)
    .exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}"))
		.pause(7)
  }
  
  object ListTrainers {
    val listTrainers = exec(http("ListTrainers")
			.get("/admin/trainers")
			.headers(headers_0))
		.pause(19)
  }
  
  object ShowTrainerThomasMoon {
    val showTrainerThomasMoon = exec(http("ShowTrainerThomasMoon")
			.get("/admin/trainers/2")
			.headers(headers_0))
		.pause(13)
  }
  
  object TrainerDeletedSuccessfully {
    val trainerDeletedSuccessfully = exec(http("TrainerDeletedSuccessfully")
			.get("/admin/trainers/2/delete")
			.headers(headers_0))
		.pause(32)
  }
  
  object ShowTrainerJohnDoe {
    val showTrainerJohnDoe = exec(http("ShowTrainerJohnDoe")
			.get("/admin/trainers/1")
			.headers(headers_0))
		.pause(10)
  }
  
  object TrainerNotDeleted {
    val trainerNotDeleted = exec(http("TrainerNotDeleted")
			.get("/admin/trainers/1/delete")
			.headers(headers_0))
		.pause(10)
  }

	val trainerManagementPositiveScn = scenario("TrainerManagementPositive").exec(Home.home,
                                                                                Login.login,
                                                                                ListTrainers.listTrainers,
                                                                                ShowTrainerThomasMoon.showTrainerThomasMoon,
                                                                                TrainerDeletedSuccessfully.trainerDeletedSuccessfully)
  
  val trainerManagementNegativeScn = scenario("TrainerManagementNegative").exec(Home.home,
                                                                                Login.login,
                                                                                ListTrainers.listTrainers,
                                                                                ShowTrainerJohnDoe.showTrainerJohnDoe,
                                                                                TrainerNotDeleted.trainerNotDeleted)

	setUp(trainerManagementPositiveScn.inject(rampUsers(3000) during (100 seconds)),
          trainerManagementNegativeScn.inject(rampUsers(3000) during (100 seconds))
       ).protocols(httpProtocol)
	.assertions(
    global.responseTime.max.lt(5000),
    global.responseTime.mean.lt(1000),
    global.successfulRequests.percent.gt(95)
  )
}