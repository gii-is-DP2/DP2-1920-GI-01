package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class TrainerManagementStepDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	@When("I delete an existing trainer called {string}")
	public void i_delete_an_existing_trainer_called(String trainer) {
		getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
		getDriver().findElement(By.xpath("//a[contains(text(),'Manage trainers')]")).click();
		getDriver().findElement(By.xpath("//a[contains(text(),'" + trainer + "')]")).click();
		getDriver().findElement(By.xpath("//a[contains(text(),'Delete')]")).click();
	}
	
	@Then("a message {string} appears in the listing")
	public void a_message_appears_in_the_listing (String message) {
		assertEquals(message, getDriver().findElement(By.xpath("//h3")).getText());
		stopDriver();
	}
	
}
