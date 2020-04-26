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
public class VetManagementStepDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;
	
	@When("I update an existing vet called {string} with a new first name {string}")
	public void i_update_an_existing_vet_called_with_a_new_first_name(String vet, String newFirstName) {
		getDriver().findElement(By.cssSelector("a.dropdown-toggle")).click();
		getDriver().findElement(By.xpath("//a[contains(text(),'Manage veterinarians')]")).click();
		getDriver().findElement(By.xpath("//a[contains(text(),'" + vet + "')]")).click();
		getDriver().findElement(By.xpath("//a[contains(text(),'Edit')]")).click();
		
		getDriver().findElement(By.id("firstName")).click();
		getDriver().findElement(By.id("firstName")).clear();
		getDriver().findElement(By.id("firstName")).sendKeys(newFirstName);
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	@Then("the updated vet called {string} appears in the listing")
	public void the_updated_vet_called_appears_in_the_listing (String newVet) {
		getDriver().findElement(By.linkText(newVet)).click();
		assertEquals(newVet, getDriver().findElement(By.xpath("//b")).getText());
		stopDriver();
	}
	
	@Then("an error appears in the vet form")
	public void an_error_appears_in_the_vet_form() {
		assertEquals("must not be empty", getDriver().findElement(By.xpath("//form[@id='add-vet-form']/div/div/div/span[2]")).getText());
		stopDriver();
	}
	
}
