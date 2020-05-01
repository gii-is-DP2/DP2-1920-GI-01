package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class PetAdoptionStepDefinitions extends AbstractStep {

	@When("I adopt a new pet to an owner called {string}")
	public void i_adopt_a_new_pet_to_an_owner_called(String owner) {
		getDriver().findElement(By.xpath("//strong[contains(text(),'vet1')]")).click();
		getDriver().findElement(By.linkText("Manage homeless pets")).click();
		getDriver().findElement(By.linkText("Adopt")).click();
		if(!owner.equals("")) {
			new Select(getDriver().findElement(By.id("owner"))).selectByVisibleText(owner);
			getDriver().findElement(By.xpath("//option[@value='" + owner + "']")).click();	
		}
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	@Then("the new pet appears in the listing")
	public void the_new_pet_appears_in_the_listing() {
		assertEquals("Tucker", getDriver().findElement(By.xpath("//tr[3]/td/dl/dd")).getText());
		assertEquals("2018-06-08", getDriver().findElement(By.xpath("//tr[3]/td/dl/dd[2]")).getText());
		assertEquals("dog", getDriver().findElement(By.xpath("//tr[3]/td/dl/dd[3]")).getText());
		stopDriver();
	}
	
	@Then("an error appears in the adoption form")
	public void an_error_appears_in_the_adoption_form() {
		assertEquals("no puede ser null",
				getDriver().findElement(By.xpath("//form[@id='adoption']/div/div/div/div/span[2]")).getText());
		stopDriver();
	}
	
}
