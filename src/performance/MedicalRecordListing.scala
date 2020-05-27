package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MedicalRecordListing extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.ico""", """.*.png""", """.*.js""", """.*.jar""", """.*.css"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_2 = Map("Origin" -> "http://www.dp2.com")

	object Home {
	  val home = exec(http("home")
		.get("/"))
		.pause(35)
	}

	object Login {
	  val login = exec(http("login")
			.get("/login"))
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(18)
		.exec(http("logged")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "vet1")
			.formParam("password", "v3t1")
			.formParam("_csrf", "${stoken}"))
		.pause(15)
	}

	object FindOwners {
	  val findowners = exec(http("findowners")
			.get("/owners/find"))
		.pause(24)
	}

	object ListOwners {
	  val listowners = exec(http("listowners")
			.get("/owners?lastName="))
		.pause(8)
	}

	object OwnerView {
	  val ownerview = exec(http("ownerview")
			.get("/owners/6"))
		.pause(10)
	}

	object MedicalHistory {
	  val medicalhistory = exec(http("medicalhistory")
			.get("/owners/6/pets/7/medical-history"))
		.pause(18)
	}

	val medicalRecordListingScn = scenario("MedicalRecordListing").exec(Home.home, Login.login, FindOwners.findowners, ListOwners.listowners, OwnerView.ownerview, MedicalHistory.medicalhistory)

	setUp(medicalRecordListingScn.inject(atOnceUsers(1))).protocols(httpProtocol)
}