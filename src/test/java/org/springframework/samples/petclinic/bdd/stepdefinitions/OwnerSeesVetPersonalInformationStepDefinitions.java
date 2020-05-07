
package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class OwnerSeesVetPersonalInformationStepDefinitions extends AbstractStep {

	@When("I go to the profile of the Veterinarian {String}")
	public void i_go_profile_veterinarian(final String name) {
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		this.getDriver().findElement(By.xpath("//a[contains(text(),'" + name + "')]")).click();
	}

	@Then("his name {String} appears in the page")
	public void name_veterinarian_check(final String name) {
		Assert.assertEquals(name, this.getDriver().findElement(By.xpath("//b")).getText());
		this.stopDriver();
	}

	@Then("the loggin page appears")
	public void an_error_appears_in_the_intervention_form() {
		this.isElementPresent(By.xpath("//input[@id='username']"));
		this.stopDriver();
	}

	private boolean isElementPresent(final By by) {
		try {
			this.getDriver().findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}
