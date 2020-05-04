package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class MedicineCreationStepDefinitions extends AbstractStep {

	@When("I create a new medicine called {string} and maker {string} with expiration date {string} of type {string}")
	public void i_create_a_new_medicine_called_with_expiration_date_of_type(String name, String maker, String expirationDate, String type) {
		getDriver().findElement(By.xpath("//span[contains(text(),'Medicines')]")).click();
		getDriver().findElement(By.linkText("Add Medicine")).click();
		getDriver().findElement(By.id("name")).click();
		getDriver().findElement(By.id("name")).clear();
		getDriver().findElement(By.id("name")).sendKeys(name);
		getDriver().findElement(By.id("expirationDate")).click();
		getDriver().findElement(By.id("expirationDate")).sendKeys(expirationDate);
		getDriver().findElement(By.id("maker")).click();
		getDriver().findElement(By.id("maker")).click();
		getDriver().findElement(By.id("maker")).clear();
		getDriver().findElement(By.id("maker")).sendKeys(maker);
		new Select(getDriver().findElement(By.id("petType"))).selectByVisibleText(type);
		getDriver().findElement(By.xpath("//option[@value='hamster']")).click();
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	@Then("the new medicine appears in the listing")
	public void the_new_medicine_appears_in_the_listing() {
		assertEquals("Prueba", getDriver().findElement(By.xpath("//b")).getText());
		assertEquals("2030-01-01", getDriver().findElement(By.xpath("//tr[2]/td")).getText());
		assertEquals("prueba", getDriver().findElement(By.xpath("//tr[3]/td/b")).getText());
		assertEquals("hamster", getDriver().findElement(By.xpath("//tr[4]/td/b")).getText());
		stopDriver();
	}
	
	@Then("an error appears in the medicine form")
	public void an_error_appears_in_the_medicine_form() {
		assertEquals("The expiration date must be in the future",
				getDriver().findElement(By.xpath("//form[@id='medicine']/div/div[2]/div/span[2]")).getText());
		stopDriver();
	}
	
}
