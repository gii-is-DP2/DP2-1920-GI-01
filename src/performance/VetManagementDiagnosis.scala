package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class VetManagementDiagnosis extends Simulation {

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
		.pause(5)
  }
  
  object Login {
    val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]","value").saveAs("stoken"))
    ).pause(12)
    .exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin1")
			.formParam("password", "4dm1n")
			.formParam("_csrf", "${stoken}"))
		.pause(8)
  }
  
  object ListVets {
    val listVets = exec(http("ListVets")
			.get("/admin/vets")
			.headers(headers_0))
		.pause(11)
  }
  
  object ShowVetTestDummy {
    val showVetTestDummy = exec(http("ShowVetTestDummy")
			.get("/admin/vets/7")
			.headers(headers_0))
		.pause(18)
  }
  
  object ShowVetTestDummyForm {
    val showVetTestDummyForm = exec(http("ShowVetTestDummyForm")
			.get("/admin/vets/7/edit")
			.headers(headers_0)
      .check(css("input[name=_csrf]","value").saveAs("stoken"))
    ).pause(21)
    .exec(http("VetTestDummyUpdated")
			.post("/admin/vets/7/edit")
			.headers(headers_3)
			.formParam("id", "7")
			.formParam("firstName", "Test2")
			.formParam("lastName", "Dummy")
			.formParam("_specialties", "1")
			.formParam("user.username", "vet7")
			.formParam("user.password", "v3t7")
			.formParam("_csrf", "${stoken}"))
		.pause(22)
  }
  
  object ShowVetJamesCarter {
    val showVetJamesCarter = exec(http("ShowVetJamesCarter")
			.get("/admin/vets/1")
			.headers(headers_0))
		.pause(20)
  }
  
  object ShowVetJamesCarterForm {
    val showVetJamesCarterForm = exec(http("ShowVetJamesCarterForm")
			.get("/admin/vets/1/edit")
			.headers(headers_0)
      .check(css("input[name=_csrf]","value").saveAs("stoken"))
    ).pause(21)
    .exec(http("ShowVetJamesCarterFormWithErrorMessage")
			.post("/admin/vets/1/edit")
			.headers(headers_3)
			.formParam("id", "1")
			.formParam("firstName", "")
			.formParam("lastName", "Carter")
			.formParam("_specialties", "1")
			.formParam("user.username", "vet1")
			.formParam("user.password", "v3t1")
			.formParam("_csrf", "${stoken}"))
		.pause(18)
  }

	val vetManagementPositiveScn = scenario("VetManagementPositive").exec(Home.home,
                                                                       Login.login,
                                                                       ListVets.listVets,
                                                                       ShowVetTestDummy.showVetTestDummy,
                                                                       ShowVetTestDummyForm.showVetTestDummyForm)
  
  val vetManagementNegativeScn = scenario("VetManagementNegative").exec(Home.home,
                                                                       Login.login,
                                                                       ListVets.listVets,
                                                                       ShowVetJamesCarter.showVetJamesCarter,
                                                                       ShowVetJamesCarterForm.showVetJamesCarterForm)
		

	setUp(vetManagementPositiveScn.inject(rampUsers(1500) during (100 seconds)),
          vetManagementNegativeScn.inject(rampUsers(1500) during (100 seconds))
       ).protocols(httpProtocol)
	.assertions(
    global.responseTime.max.lt(5000),
    global.responseTime.mean.lt(1000),
    global.successfulRequests.percent.gt(95)
  )
}