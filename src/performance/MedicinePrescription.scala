package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MedicinePrescription extends Simulation {

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

	val headers_4 = Map(
		"Accept" -> "*/*",
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
			.check(css("input[name=_csrf]","value").saveAs("stoken"))))
		.pause(16)
		.exec(http("logged")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "vet1")
			.formParam("password", "v3t1")
			.formParam("_csrf", "${stoken}"))
		.pause(9)
	}

	object OwnerList {
		var ownerList = exec(http("OwnerList")
			.get("/owners/find")
			.headers(headers_0))
		.pause(13)
	}

	object OwnerListing {
		var ownerListing = exec(http("OwnerListing")
			.get("/owners?lastName=")
			.headers(headers_0))
		.pause(18)
	}

	object OwnerSelection {
		var ownerSelection = exec(http("OwnerSelection")
			.get("/owners/6")
			.headers(headers_0))
		.pause(18)
	}

	object MedicalRecordListing {
		var medicalRecordListing = exec(http("MedicalRecordListing")
			.get("/owners/6/pets/7/medical-history")
			.headers(headers_0))
		.pause(9)
	}

	object MedicalRecordSelection {
		var medicalRecordSelection = exec(http("MedicalRecordSelection")
			.get("/owners/6/pets/7/visits/1/medical-record/show?id=1")
			.headers(headers_0))
		.pause(13)
	}

	object MedicinePrescriptionFormPositive {
		var medicinePrescriptionFormPositive = exec(http("PrescriptionForm")
			.get("/owners/6/pets/7/visits/1/medical-record/1/prescription/create")
			.headers(headers_0))
			.check(css("input[name=_csrf]","value").saveAs("stoken"))))
		.pause(15)
		.exec(http("PrescriptionCreated")
			.post("/owners/6/pets/7/visits/1/medical-record/1/prescription/create")
			.headers(headers_3)
			.formParam("id", "")
			.formParam("medicine", "Cat medicine")
			.formParam("dose", "prueba")
			.formParam("_csrf", "${stoken}"))
		.pause(6)
	}

	object AddPrescriptionFormNegative {
		var medicinePrescriptionFormNegative = exec(http("PrescriptionForm")
			.get("/owners/6/pets/7/visits/1/medical-record/1/prescription/create")
			.headers(headers_0))
		.pause(3)
		.exec(http("PrescriptionCreationFormWithErrorMessage")
			.post("/owners/6/pets/7/visits/1/medical-record/1/prescription/create")
			.headers(headers_3)
			.formParam("id", "")
			.formParam("dose", "prueba")
			.formParam("_csrf", "6c2eb6bd-254b-4369-b765-800875c4ebb3"))
		.pause(9)
	}

	val medicinePrescriptionPositiveScn = scenario("MedicinePrescriptionPositive").exec(Home.home,
																						Login.login,
																						OwnerList.ownerList,
																						OwnerListing.ownerListing,
																						OwnerSelection.ownerSelection,
																						MedicalRecordListing.medicalRecordListing,
																						MedicalRecordSelection.medicalRecordSelection,
																						MedicinePrescriptionFormPositive.medicinePrescriptionFormPositive
																						)

	val medicinePrescriptionNegativeScn = scenario("MedicinePrescriptionNegative").exec(Home.home,
																						Login.login,
																						OwnerList.ownerList,
																						OwnerListing.ownerListing,
																						OwnerSelection.ownerSelection,
																						MedicalRecordListing.medicalRecordListing,
																						MedicalRecordSelection.medicalRecordSelection,
																						MedicinePrescriptionFormNegative.medicinePrescriptionFormNegative
																						)

	setUp(medicinePrescriptionPositiveScn.inject(atOnceUsers(1)),
		  medicinePrescriptionNegativeScn.inject(atOnceUsers(1)))
		.protocols(httpProtocol)
}