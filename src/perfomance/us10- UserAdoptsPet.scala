package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class us20 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.jar""", """.*.css""", """.*.js""", """.*.png""", """.*.ico"""), WhiteList())
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



	val scn = scenario("us20")
		.exec(http("request_0")
			.get("/")
			.headers(headers_0))
		.pause(12)
		// home
		.exec(http("login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)))
		.pause(11)
		// login
		.exec(http("logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "vet1")
			.formParam("password", "v3t1")
			.formParam("_csrf", "5f41e2e9-505b-4b35-90eb-08579571d938"))
		.pause(15)
		// logged
		.exec(http("adoptions")
			.get("/homeless-pets")
			.headers(headers_0))
		.pause(7)
		// adoptions
		.exec(http("adoptionForm")
			.get("/homeless-pets/14/adopt")
			.headers(headers_0))
		.pause(37)
		// adoptionForm
		.exec(http("adopt")
			.post("/homeless-pets/14/adopt")
			.headers(headers_3)
			.formParam("owner", "George Franklin")
			.formParam("_csrf", "46c34adb-641c-489a-b36b-92884e86ffb1"))
		.pause(4)
		// adopt

	setUp(scn.inject(atOnceUsers(1500))).protocols(httpProtocol)
}