
package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class VetCreatesMedicalRecordStepDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;


	@When("I add a new medical record with description {string} and status {string}")
	public void i_add_a_new_medical_record_with_description_and_status(final String description, final String status) {
		new WebDriverWait(this.getDriver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.dropdown-toggle")));
		this.getDriver().get("http://localhost:" + this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		this.getDriver().findElement(By.linkText("Jean Coleman")).click();
		this.getDriver().findElement(By.xpath("(//a[contains(text(),'Add Medical Record')])[3]")).click();
		this.getDriver().findElement(By.id("description")).click();
		this.getDriver().findElement(By.id("description")).clear();
		this.getDriver().findElement(By.id("description")).sendKeys(description);
		this.getDriver().findElement(By.id("status")).click();
		this.getDriver().findElement(By.id("status")).clear();
		this.getDriver().findElement(By.id("status")).sendKeys(status);
		this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}

	@Then("The new medical record with status {string} and description {string} is shown")
	public void the_new_medical_record_is_shown(final String status, final String description) {
		Assert.assertEquals(status, this.getDriver().findElement(By.xpath("//tr[2]/td")).getText());
		Assert.assertEquals(description, this.getDriver().findElement(By.xpath("//b")).getText());
		this.stopDriver();
	}

	@Then("an error appears in the medical record form")
	public void an_error_appears_in_the_medical_record_form() {
		Assert.assertEquals("no puede estar vacío", this.getDriver().findElement(By.xpath("//form[@id='medicalRecord']/div/div[2]/div/span[2]")).getText());
		Assert.assertEquals("no puede estar vacío", this.getDriver().findElement(By.xpath("//form[@id='medicalRecord']/div/div[3]/div/span[2]")).getText());
		this.stopDriver();
	}

}