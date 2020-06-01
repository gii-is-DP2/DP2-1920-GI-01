package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class InterventionCreateDiagnosis extends Simulation {

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

     object InterventionFormPositive {
    
    val feederPositive = csv("InterventionCreationPositive.csv")
    
    val homelessPetFormPositive = exec(http("InterventionFormPositive")
			.get("/owners/*/pets/1/interventions/new")
			.headers(headers_0)
      .check(css("input[name=_csrf]","value").saveAs("stoken"))
    ).pause(34)
    .feed(feederPositive)
    .exec(http("InterventionCreated")
			.post("/owners/*/pets/1/interventions/new")
			.headers(headers_0)
			.formParam("id", "")
			.formParam("intervention_date", "${intervention_date}")
			.formParam("intervention_time", "${intervention_time}")
			.formParam("intervention_description", "${intervention_description}")
			.formParam("_csrf", "${stoken}"))
		.pause(5)
  }

  object IntervntionFormNegative {
		var feederNegative = csv("InterventionCreationNegative.csv")

		var interventionCreationFormNegative = exec(http("InterventionForm")
			.get("/owners/*/pets/1/interventions/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken")))
		.pause(26)
		.feed(feederNegative)
		.exec(http("InterventionCreationFormWithErrorMessage")
			.post("/owners/*/pets/1/interventions/new")
			.headers(headers_0)
			.formParam("id", "")
			.formParam("intervention_date", "${intervention_date}")
			.formParam("intervention_time", "${intervention_time}")
			.formParam("intervention_description", "${intervention_description}")
			.formParam("_csrf", "${stoken}"))
		.pause(10)
	}

    val interventionCreationPositiveScn = scenario("InterventionCreationPositive").exec(Home.home,
																				Login.login,
																				InterventionCreationFormPositive.interventionCreationFormPositive)

	val interventionCreationNegativeScn = scenario("InterventionCreationNegative").exec(Home.home,
																				Login.login,
																				InterventionCreationFormNegative.interventionCreationFormNegative)																			

	setUp(interventionCreationPositiveScn.inject(rampUsers(40) during (50 seconds)),
          interventionCreationNegativeScn.inject(rampUsers(40) during (50 seconds)))
		.protocols(httpProtocol)
		.assertions(
			global.responseTime.max.lt(5000),
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95))
}