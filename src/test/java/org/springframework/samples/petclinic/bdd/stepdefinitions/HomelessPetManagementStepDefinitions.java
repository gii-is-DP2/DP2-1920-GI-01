package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomelessPetManagementStepDefinitions extends AbstractStep{

	@LocalServerPort
	private int port;
	
	@Given("I am logged in the system as {string} with password {string}")
	public void i_am_logged_in_the_system_as_with_password(String username, String password) {
		LoginStepDefinitions.loginAs(username, password, port, getDriver());
	}
	
	@When("I add a new homeless pet called {string} born on {string} of type {string}")
	public void i_add_a_new_homeless_pet_called_born_on_of_type(String name, String birthdate, String type) {
		new WebDriverWait(getDriver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.dropdown-toggle")));
		getDriver().findElement(By.cssSelector("a.dropdown-toggle")).click();
		getDriver().findElement(By.xpath("//a[contains(text(),'Manage homeless pets')]")).click();
		getDriver().findElement(By.xpath("//a[contains(text(),'Add a Pet')]")).click();
		
		getDriver().findElement(By.id("name")).click();
		getDriver().findElement(By.id("name")).clear();
		getDriver().findElement(By.id("name")).sendKeys(name);
		getDriver().findElement(By.id("birthDate")).click();
		getDriver().findElement(By.id("birthDate")).clear();
		getDriver().findElement(By.id("birthDate")).sendKeys(birthdate);
		getDriver().findElement(By.xpath("(//a[contains(@href, '#')])[2]")).click();
		new Select(getDriver().findElement(By.id("type"))).selectByVisibleText(type);
		getDriver().findElement(By.xpath("//option[@value='" + type + "']")).click();
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	@Then("the new homeless pet called {string} appears in the listing")
	public void the_new_homeless_pet_appears_in_the_listing(String name) {
		assertEquals(name, getDriver().findElement(By.xpath("//tr[3]/td/a")).getText());
	}
	
}
