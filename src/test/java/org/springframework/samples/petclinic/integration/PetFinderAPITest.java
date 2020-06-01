package org.springframework.samples.petclinic.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.API.resources.PetfinderResource;

import io.restassured.http.ContentType;
import lombok.extern.java.Log;

@Log
class PetFinderAPITest {
	
	private String token = PetfinderResource.getToken();
	
	@Test
	void getTokenTest() {
		given()
			.contentType(ContentType.JSON)
				.body("{\r\n" + 
					"  \"client_id\": \"EORSI4EeIBk7izNDQOKxDYRtAggUKjN0JpR8tGPlFfrcBdFcs8\",\r\n" + 
					"  \"client_secret\": \"fn2zmavJpNwmCNZeOBlzm1imqBAFRwGH5CKIR3fV\",\r\n" + 
					"  \"grant_type\": \"client_credentials\"\r\n" + 
					"  }")
		.when()
			.post("https://api.petfinder.com/v2/oauth2/token")
		.then()
			.statusCode(200)
		.and()
			.assertThat()
				.body("token_type", equalTo("Bearer"))
				.body("expires_in", equalTo(3600))
				.body("access_token", not(emptyString()))
		.and()
			.time(lessThan(20L), TimeUnit.SECONDS);
	}
	
	@Test
	void getOrganizationsListTest() {
		given()
			.auth()
				.oauth2(token)
		.when()
			.get("https://api.petfinder.com/v2/organizations")
		.then()
			.statusCode(200)
		.and()
			.assertThat()
				.body("organizations.size()", equalTo(20))
				.body("pagination.count_per_page", equalTo(20))
				.body("pagination.current_page", equalTo(1))
		.and()
			.time(lessThan(20L), TimeUnit.SECONDS);
	}	
	
	@Test
	void getOrganizationTest() {
		given()
			.auth()
				.oauth2(token)
			.pathParam("id", "MA33")
		.when()
			.get("https://api.petfinder.com/v2/organizations/{id}")
		.then()
			.statusCode(200)
		.and()
			.assertThat()
				.body("organization.id", equalTo("MA33"))
				.body("organization.name", equalTo("Kitty Angels"))
				.body("organization.email", equalTo("info@kittyangels.org"))
				.body("organization.phone", equalTo("(978) 649-4681"))
				.body("organization.address.city", equalTo("Tyngsboro"))
				.body("organization.url", equalTo("https://www.petfinder.com/member/us/ma/tyngsboro/kitty-angels-ma33/?referrer_id=a70363be-703e-4c08-b1b2-184785d392a6"))
		.and()
			.time(lessThan(20L), TimeUnit.SECONDS);
	}
	
	
}
