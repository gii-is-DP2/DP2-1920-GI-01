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
public class RehabManagementStepDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;

	
	@When ("I create a new rehabilitation for a pet whose owner last name is {string}")
	public void i_create_a_new_rehabilitation_for_a_pet_whose_owner_last_name_is(String lastName) {

		getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		getDriver().findElement(By.name("lastName")).click();
		getDriver().findElement(By.name("lastName")).clear();
		getDriver().findElement(By.name("lastName")).sendKeys("Rodriquez");
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		getDriver().findElement(By.xpath("(//a[contains(text(),'Add Rehab')])[2]")).click();
		
	}

	@Then ("a new rehabilitation with a description {string} appears in the list")
	public void a_new_rehabilitation_with_a_description_appears_in_the_list (String description) {
		getDriver().findElement(By.id("time")).click();
		getDriver().findElement(By.id("time")).clear();
		getDriver().findElement(By.id("time")).sendKeys("3");
		getDriver().findElement(By.id("description")).click();
		getDriver().findElement(By.id("description")).clear();
		getDriver().findElement(By.id("description")).sendKeys("First rehab");
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		getDriver().findElement(By.xpath("//td[3]")).click();
		assertEquals(description, getDriver().findElement(By.xpath("//td[3]")).getText());
	    stopDriver();
	}
	

	@Then ("an error appears in the rehabilitation form")
	public void an_error_appears_in_the_rehabilitation_form() {
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	    assertEquals("must not be empty", getDriver().findElement(By.xpath("//form[@id='rehab']/div/div[3]/div")).getText());
	    stopDriver();
	}
	

	
}
