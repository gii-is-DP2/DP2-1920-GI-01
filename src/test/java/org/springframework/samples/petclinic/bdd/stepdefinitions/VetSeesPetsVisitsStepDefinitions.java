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
public class VetSeesPetsVisitsStepDefinitions extends AbstractStep{

	@LocalServerPort
	private int port;
	
	@When ("I go to the {string} page")
	public void i_go_to_the_page(String findowners) {
	
		getDriver().findElement(By.xpath("//span[contains(text(),'Find owners')]")).click();
		getDriver().findElement(By.xpath("//h2")).click();
	    //assertEquals(findowners, getDriver().findElement(By.xpath("//span[contains(text(),'FIND OWNERS')]")).getText());
			
	}
	
	@Then ("After finding owner {string} I can see his pets visits in the section {string}")
	public void after_finding_owner_i_can_see_his_pets_visits_in_the_section(String owner, String section) {
		
		getDriver().findElement(By.name("lastName")).click();
		getDriver().findElement(By.name("lastName")).clear();
		getDriver().findElement(By.name("lastName")).sendKeys("Rodriquez");
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals(owner, getDriver().findElement(By.xpath("//td")).getText());
		assertEquals(section, getDriver().findElement(By.xpath("//h2[2]")).getText());
		stopDriver();
	}
	
	
	@Then ("an error appears {string}")
	public void an_error_appears(String error) {
		getDriver().findElement(By.name("lastName")).click();
		getDriver().findElement(By.name("lastName")).clear();
		getDriver().findElement(By.name("lastName")).sendKeys(" Rodriquez");
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		getDriver().findElement(By.xpath("//div[@id='lastName']/div")).click();
		assertEquals(error, getDriver().findElement(By.xpath("//div[@id='lastName']/div")).getText());
		stopDriver();
	}
	
	}
	
	

