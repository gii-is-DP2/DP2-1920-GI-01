package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class us15 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.jar""", """.*.css""", """.*.png""", """.*.ico""", """.*.js"""), WhiteList())
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



	val scn = scenario("home")
		.exec(http("request_0")
			.get("/")
			.headers(headers_0))
		.pause(8)
		// home
		.exec(http("login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)))
		.pause(10)
		// login
		.exec(http("logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "vet1")
			.formParam("password", "v3t1")
			.formParam("_csrf", "b4d087f5-5169-4092-b0e4-522a60faa6d4"))
		.pause(13)
		// logged
		.exec(http("findOwners")
			.get("/owners/find")
			.headers(headers_0))
		.pause(8)
		// findOwners
		.exec(http("ownerList")
			.get("/owners?lastName=")
			.headers(headers_0))
		.pause(6)
		// ownerList
		.exec(http("ownerShow")
			.get("/owners/1")
			.headers(headers_0))
		.pause(10)
		// ownerShow
		.exec(http("addIntervention")
			.get("/owners/1/pets/1/interventions/new")
			.headers(headers_0))
		.pause(14)
		// addIntervention
		.exec(http("add")
			.post("/owners/1/pets/1/interventions/new")
			.headers(headers_3)
			.formParam("interventionDescription", "Wing intervention")
			.formParam("interventionDate", "2020/09/20")
			.formParam("interventionTime", "1")
			.formParam("petId", "1")
			.formParam("_csrf", "25b60d4c-9b48-42cf-b275-6f83b0ce7c2f"))
		.pause(3)
		// add

	setUp(scn.inject(atOnceUsers(1500))).protocols(httpProtocol)
}
