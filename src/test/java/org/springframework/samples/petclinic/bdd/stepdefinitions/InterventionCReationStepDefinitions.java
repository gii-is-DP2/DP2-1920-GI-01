
package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.junit.Assert;
import org.openqa.selenium.By;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class InterventionCReationStepDefinitions extends AbstractStep {

	@When("I create a new intervention with description {string} and intervention time {string} with intervention date {string}")
	public void i_create_a_new_intervention_with_description_and_time_with_intervention_date(final String description, final String time, final String date) {
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		this.getDriver().findElement(By.name("lastName")).click();
		this.getDriver().findElement(By.name("lastName")).clear();
		this.getDriver().findElement(By.name("lastName")).sendKeys("Franklin");
		this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		this.getDriver().findElement(By.linkText("Add Intervention")).click();
		this.getDriver().findElement(By.id("interventionTime")).click();
		this.getDriver().findElement(By.id("interventionTime")).clear();
		this.getDriver().findElement(By.id("interventionTime")).sendKeys(time);
		this.getDriver().findElement(By.id("interventionDescription")).click();
		this.getDriver().findElement(By.id("interventionDescription")).clear();
		this.getDriver().findElement(By.id("interventionDescription")).sendKeys(description);
		this.getDriver().findElement(By.id("interventionDate")).click();
		this.getDriver().findElement(By.id("interventionDate")).clear();

		// The intervention date bellow may provoke a failure if the test is runned after the date provided, if so, change the date to a future date

		this.getDriver().findElement(By.id("interventionDate")).sendKeys(date);
		this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}

	@Then("the new intervention appears in the owners details page")
	public void the_new_intervention_appears_in_the_owners_details_page() {
		Assert.assertEquals("Descripcion", this.getDriver().findElement(By.xpath("//table[3]/tbody/tr/td[2]/table/tbody/tr/td[2]")).getText());
		Assert.assertEquals("2020-10-01", this.getDriver().findElement(By.xpath("//table[3]/tbody/tr/td[2]/table/tbody/tr/td")).getText());
		Assert.assertEquals("1", this.getDriver().findElement(By.xpath("//td[3]")).getText());
		this.stopDriver();
	}

	@Then("an error appears in the intervention form")
	public void an_error_appears_in_the_intervention_form() {
		Assert.assertEquals("The  date must be in the future", this.getDriver().findElement(By.xpath("//form[@id='intervention']/div/div[2]/div/span[2]")).getText());
		this.stopDriver();
	}
}
