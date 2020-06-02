package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class PetAdoption extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.ico""", """.*.png""", """.*.js""", """.*.css"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8")
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
		var home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(35)
	}

	object Login {
		var login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken")))
		.pause(16)
		.exec(http("logged")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "vet1")
			.formParam("password", "v3t1")
			.formParam("_csrf", "${stoken}"))
		.pause(9)
	}

	object ListHomelessPets {
		var listHomelessPets = exec(http("ListHomelessPets")
			.get("/homeless-pets")
			.headers(headers_0))
		.pause(19)
	}

	object PetAdoptionFormPositive {
		var petAdoptionFormPositive = exec(http("PetAdoptionForm")
			.get("/homeless-pets/14/adopt")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken")))
		.pause(13)
		.exec(http("PetAdopted")
			.post("/homeless-pets/14/adopt")
			.headers(headers_3)
			.formParam("owner", "George Franklin")
			.formParam("_csrf", "${stoken}"))
		.pause(21)
	}

	object AdoptionHistory {
		var adoptionHistory = exec(http("AdoptionHistory")
			.get("/owners/1/pets/14/adoption-history")
			.headers(headers_0))
		.pause(21)
	}

	object PetAdoptionFormNegative {
		var petAdoptionFormNegative = exec(http("PetAdoptionForm")
			.get("/homeless-pets/15/adopt")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken")))
		.pause(2)
		.exec(http("PetAdoptionFormWithErrorMessage")
			.post("/homeless-pets/15/adopt")
			.headers(headers_3)
			.formParam("_csrf", "${stoken}"))
		.pause(4)
	}

	val petAdoptionPositiveScn = scenario("PetAdoptionPositive").exec(Home.home,
																	  Login.login,
																	  ListHomelessPets.listHomelessPets,
																	  PetAdoptionFormPositive.petAdoptionFormPositive,
																	  AdoptionHistory.adoptionHistory)

	val petAdoptionNegativeScn = scenario("PetAdoptionNegative").exec(Home.home,
																	  Login.login,
																	  ListHomelessPets.listHomelessPets,
																	  PetAdoptionFormNegative.petAdoptionFormNegative)

	setUp(petAdoptionPositiveScn.inject(atOnceUsers(42500)),
		  petAdoptionNegativeScn.inject(atOnceUsers(42500)))
		.protocols(httpProtocol)
}