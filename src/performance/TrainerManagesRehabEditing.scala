package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class TrainerManagesRehabEditing extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("lv-LV,lv;q=0.9,en-US;q=0.8,en;q=0.7")
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
		.pause(8)
}

object Login {
val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)))
		.pause(16)
}

object Logged {
val logged = exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "trainer1")
			.formParam("password", "tr41n3r")
			.formParam("_csrf", "ab87a3cc-ef0f-46a7-8db8-83c16977a3f8"))
		.pause(8)
}

object FindOwners {
val findowners = exec(http("FindOwners")
			.get("/owners/find")
			.headers(headers_0))
		.pause(18)
}

object OwnerInformation {
val ownerinformation = exec(http("OwnerInformation")
			.get("/owners?lastName=Rodriquez")
			.headers(headers_0))
		.pause(15)
}

object NewRehab {
val newrehab = exec(http("NewRehab")
			.get("/owners/3/pets/3/rehab/new")
			.headers(headers_0))
		.pause(18)
}


object AddedNewRehab {
val addednewrehab = exec(http("AddedNewRehab")
			.post("/owners/3/pets/3/rehab/new")
			.headers(headers_3)
			.formParam("petId", "3")
			.formParam("id", "")
			.formParam("date", "2020/05/18")
			.formParam("time", "1")
			.formParam("description", "test")
			.formParam("_csrf", "a1dcc363-6db6-4638-8637-cd79a034be0f"))
		.pause(13)
}

object EditRehabSuccessful {
val editrehabsuccessful = exec(http("EditRehabSuccessful")
			.get("/owners/3/pets/3/rehab/4/edit")
			.headers(headers_0))
		.pause(32)
}

object SuccesfulRehabEditing {
val successfulrehabediting = exec(http("SuccesfulRehabEditing")
			.post("/owners/3/pets/3/rehab/4/edit")
			.headers(headers_3)
			.formParam("petId", "3")
			.formParam("id", "4")
			.formParam("date", "2020/05/18")
			.formParam("time", "3")
			.formParam("description", "test edit successful")
			.formParam("_csrf", "a1dcc363-6db6-4638-8637-cd79a034be0f"))
		.pause(24)
}

object NewRehab2 {

val newrehab2 = exec(http("NewRehab2")
			.get("/owners/3/pets/3/rehab/new")
			.headers(headers_0))
		.pause(18)

}

object AddedNewRehab2 {
val newrehab2 = exec(http("AddedNewRehab2")
			.post("/owners/3/pets/3/rehab/new")
			.headers(headers_3)
			.formParam("petId", "3")
			.formParam("id", "")
			.formParam("date", "2020/05/18")
			.formParam("time", "3")
			.formParam("description", "test 2")
			.formParam("_csrf", "a1dcc363-6db6-4638-8637-cd79a034be0f"))
		.pause(12)
}

object UnseccessfulEditing {
val unsuccessfulediting = exec(http("UnseccessfulEditing")
			.get("/owners/3/pets/3/rehab/6/edit")
			.headers(headers_0))
		.pause(16)

}

object request_13 {
val error = exec(http("request_13")
			.post("/owners/3/pets/3/rehab/6/edit")
			.headers(headers_3)
			.formParam("petId", "3")
			.formParam("id", "6")
			.formParam("date", "2020/05/18")
			.formParam("time", "3")
			.formParam("description", "")
			.formParam("_csrf", "a1dcc363-6db6-4638-8637-cd79a034be0f"))
}

	val SuccessfulRehabEditing = scenario("Successful").exec(
	Home.home,
	Login.login,
	Logged.logged,
	FindOwners.findowners,
	OwnerInformation.ownerinformation,
	NewRehab.newrehab,
	AddedNewRehab.addednewrehab,
	EditRehabSuccessful.editrehabsuccessful,
	SuccesfulRehabEditing.successfulrehabediting
	)

	val UnsuccessfulRehabEditing = scenario("Unsuccessful").exec(
	Home.home,
	Login.login,
	Logged.logged,
	FindOwners.findowners,
	OwnerInformation.ownerinformation,
	NewRehab2.newrehab2,
	AddedNewRehab2.newrehab2,
	UnseccessfulEditing.unsuccessfulediting,
	request_13.error
	)
		
	setUp(SuccessfulRehabEditing.inject(atOnceUsers(1)),
	      UnsuccessfulRehabEditing.inject(atOnceUsers(1))

	).protocols(httpProtocol)	

	
}