package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MedicineCreate extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.ico""", """.*.png""", """.*.js""", """.*.css"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map("Proxy-Connection" -> "keep-alive")

	val headers_2 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive")

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

	object MedicineListing {
		var medicineListing = exec(http("MedicineListing")
			.get("/medicine/list")
			.headers(headers_0))
		.pause(16)
	}
	
	object MedicineCreationFormPositive {
		var medicineCreationFormPositive = exec(http("MedicineForm")
			.get("/medicine/create")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken")))
		.pause(26)
		.exec(http("MedicineCreated")
			.post("/medicine/create")
			.headers(headers_2)
			.formParam("name", "Prueba")
			.formParam("expirationDate", "2020/12/12")
			.formParam("maker", "Prueba")
			.formParam("petType", "bird")
			.formParam("_csrf", "${stoken}"))
		.pause(10)
	}

	object MedicineCreationFormNegative {
		var medicineCreationFormNegative = exec(http("MedicineForm")
			.get("/medicine/create")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken")))
		.pause(26)
		.exec(http("MedicineCreationFormWithErrorMessage")
			.post("/medicine/create")
			.headers(headers_0)
			.formParam("name", "prueba")
			.formParam("expirationDate", "2020/02/01")
			.formParam("maker", "prueba")
			.formParam("petType", "dog")
			.formParam("_csrf", "${stoken}"))
		.pause(10)
	}

	val medicineCreationPositiveScn = scenario("MedicineCreationPositive").exec(Home.home,
																				Login.login,
																				MedicineListing.medicineListing,
																				MedicineCreationFormPositive.medicineCreationFormPositive)

	val medicineCreationNegativeScn = scenario("MedicineCreationNegative").exec(Home.home,
																				Login.login,
																				MedicineListing.medicineListing,
																				MedicineCreationFormNegative.medicineCreationFormNegative)																			

	setUp(medicineCreationPositiveScn.inject(atOnceUsers(45000)),
		  medicineCreationNegativeScn.inject(atOnceUsers(45000)))
		.protocols(httpProtocol)
}