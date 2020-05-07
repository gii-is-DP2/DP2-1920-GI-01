
package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.junit.Assert;
import org.openqa.selenium.By;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class VisitCreationStepDefinition extends AbstractStep {

	@When("I create a new visit with description {String} and date {String}")
	public void i_create_a_new_visit_with_description_and_date(final String description, final String date) {
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		this.getDriver().findElement(By.name("lastName")).click();
		this.getDriver().findElement(By.name("lastName")).clear();
		this.getDriver().findElement(By.name("lastName")).sendKeys("Franklin");
		this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		this.getDriver().findElement(By.linkText("Add Visit")).click();
		this.getDriver().findElement(By.id("description")).click();
		this.getDriver().findElement(By.id("description")).clear();
		this.getDriver().findElement(By.id("description")).sendKeys(description);
		this.getDriver().findElement(By.id("date")).click();
		this.getDriver().findElement(By.id("date")).clear();

		// The visit date bellow may provoke a failure if the test is runned after the date provided, if so, change the date to a future date

		this.getDriver().findElement(By.id("date")).sendKeys(date);
		this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}

	@Then("the new visit appears in the owners details page")
	public void the_new_visit_appears_in_the_owners_details_page() {
		Assert.assertEquals("Description", this.getDriver().findElement(By.xpath("//b")).getText());
		Assert.assertEquals("2020/10/01", this.getDriver().findElement(By.xpath("//tr[2]/td")).getText());
		this.stopDriver();
	}

	@Then("an error appears in the visit form")
	public void an_error_appears_in_the_visit_form() {
		Assert.assertEquals("The  date must be in the future", this.getDriver().findElement(By.xpath("//form[@id='intervention']/div/div[2]/div/span[2]")).getText());
		this.stopDriver();
	}
}
