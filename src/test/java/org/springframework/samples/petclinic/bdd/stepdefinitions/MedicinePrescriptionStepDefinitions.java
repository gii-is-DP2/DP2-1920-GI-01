package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class MedicinePrescriptionStepDefinitions extends AbstractStep {

	@When("I prescript a new medicine called {string} to a cat with a {string}")
	public void i_prescript_a_new_medicine_called_to_a_cat_with_a_test_dose(String name, String dose) {
		getDriver().findElement(By.xpath("//span[contains(text(),'Find owners')]")).click();
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		getDriver().findElement(By.linkText("Jean Coleman")).click();
		getDriver().findElement(By.xpath("(//a[contains(text(),'Medical History')])[2]")).click();
		getDriver().findElement(By.linkText("2013-01-01")).click();
		getDriver().findElement(By.linkText("Add prescription")).click();
		if(!name.equals("")) {
			new Select(getDriver().findElement(By.id("medicine"))).selectByVisibleText(name);
			getDriver().findElement(By.xpath("//option[@value='" + name + "']")).click();
		}
		getDriver().findElement(By.id("dose")).click();
		getDriver().findElement(By.id("dose")).clear();
		getDriver().findElement(By.id("dose")).sendKeys(dose);
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	@Then("the medicine appears in the corresponding medical record")
	public void the_medicine_appears_in_the_corresponding_medical_record() {
		assertEquals("test dose",
				getDriver().findElement(By.xpath("//table[@id='prescriptionsTable']/tbody/tr[2]/td[2]")).getText());
		assertEquals("Cat medicine",
				getDriver().findElement(By.xpath("(//a[contains(text(),'Cat medicine')])[2]")).getText());
		stopDriver();
	}
	
	@Then("an error appears in the prescription form")
	public void an_error_appears_in_the_prescription_form() {
		assertEquals("is required",
				getDriver().findElement(By.xpath("//form[@id='prescription']/div/div[2]/div/div/span[2]")).getText());
		stopDriver();
	}
	
}
