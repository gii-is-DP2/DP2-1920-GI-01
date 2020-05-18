package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class TrainerManagesRehabs extends Simulation {

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
		.pause(13)
}

object Login {
val login = exec(http("Login")
			.get("/login")
			.headers(headers_0))
		.pause(1)
		.exec(http("request_2")
			.get("/login")
			.headers(headers_2))
		.pause(31)
 }

object Logged {
val logged = exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "trainer1")
			.formParam("password", "tr41n3r")
			.formParam("_csrf", "cb8ea9e4-5130-46ae-89d1-08ae57b2eba5"))
		.pause(23)
}

object FindOwnersPage {
val findownerspage = exec(http("FindOwnersPage")
			.get("/owners/find")
			.headers(headers_0))
		.pause(34)
}

object OwnerDetails {
val ownerdetails = exec(http("OwnerDetails")
			.get("/owners?lastName=Rodriquez")
			.headers(headers_0))
		.pause(22)
}

object NewRehabForm {
val newrehabform = exec(http("NewRehabForm")
			.get("/owners/3/pets/3/rehab/new")
			.headers(headers_0))
		.pause(31)
}

object AddedSuccessfulNewRehab {
val addedsuccessfulnewrehab = exec(http("AddedSuccessfulNewRehab")
			.post("/owners/3/pets/3/rehab/new")
			.headers(headers_3)
			.formParam("petId", "3")
			.formParam("id", "")
			.formParam("date", "2020/05/18")
			.formParam("time", "3")
			.formParam("description", "test")
			.formParam("_csrf", "264a89f7-df63-45b2-a9e9-4c8c26dca961"))
		.pause(37)
}



object UnsuccessfulAddingOfRehab {
val unsuccessfuladdingofrehab = exec(http("UnsuccessfulAddingOfRehab")
			.post("/owners/3/pets/3/rehab/new")
			.headers(headers_3)
			.formParam("petId", "3")
			.formParam("id", "")
			.formParam("date", "2020/05/18")
			.formParam("time", "")
			.formParam("description", "test")
			.formParam("_csrf", "264a89f7-df63-45b2-a9e9-4c8c26dca961"))
		.pause(27)
}

val NewSuccessfulRehab = scenario("Successful").exec(
Home.home,
Login.login,
Logged.logged,
FindOwnersPage.findownerspage,
OwnerDetails.ownerdetails,
NewRehabForm.newrehabform,
AddedSuccessfulNewRehab.addedsuccessfulnewrehab
)


val UnsuccessfulNewRehab = scenario("Unsuccessful").exec(
Home.home,
Login.login,
Logged.logged,
FindOwnersPage.findownerspage,
OwnerDetails.ownerdetails,
NewRehabForm.newrehabform,
UnsuccessfulAddingOfRehab.unsuccessfuladdingofrehab
)


	setUp(NewSuccessfulRehab.inject(atOnceUsers(1)),
	      UnsuccessfulNewRehab.inject(atOnceUsers(1))

	).protocols(httpProtocol)

}