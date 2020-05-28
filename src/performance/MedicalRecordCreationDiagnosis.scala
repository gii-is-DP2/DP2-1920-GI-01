package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MedicalRecordCreation extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.ico""", """.*.png""", """.*.css""", """.*.js""", """.*.jar"""), WhiteList())
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


	object MedicalRecordFormPositive {
	  val medicalrecordformpositive = exec(http("medicalrecordformpositive")
			.get("/owners/6/pets/8/visits/3/medical-record/new"))
			.check(css("input[name=_csrf]","value").saveAs("stoken")))
		.pause(24)
		.exec(http("creation")
			.post("/owners/6/pets/8/visits/3/medical-record/new")
			.headers(headers_2)
			.formParam("id", "")
			.formParam("description", "testDescription")
			.formParam("status", "testStatus")
			.formParam("_csrf", "${stoken}"))
		.pause(11)
	}

	object MedicalRecordFormNegative {
	  val medicalrecordformnegative = exec(http("medicalrecordformnegative")
			.get("/owners/6/pets/8/visits/3/medical-record/new"))
			.check(css("input[name=_csrf]","value").saveAs("stoken")))
		.pause(24)
		.exec(http("error")
			.post("/owners/6/pets/8/visits/3/medical-record/new")
			.headers(headers_2)
			.formParam("id", "")
			.formParam("description", "")
			.formParam("status", "testStatus")
			.formParam("_csrf", "${stoken}"))
		.pause(11)
	}

	val medicalRecordCreationPositiveScn = scenario("MedicalRecordCreationPositive").exec(Home.home, Login.login, FindOwners.findowners, ListOwners.listowners, OwnerView.ownerview, MedicalRecordFormPositive.medicalrecordformpositive)

	val medicalRecordCreationNegativeScn = scenario("MedicalRecordCreationNegative").exec(Home.home, Login.login, FindOwners.findowners, ListOwners.listowners, OwnerView.ownerview, MedicalRecordFormNegative.medicalrecordformnegative)
	
	setUp(medicalRecordCreationNegativeScn.inject(rampUsers(250) during (100 seconds)), medicalRecordCreationNegativeScn.inject(rampUsers(250) during (100 seconds))).protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}