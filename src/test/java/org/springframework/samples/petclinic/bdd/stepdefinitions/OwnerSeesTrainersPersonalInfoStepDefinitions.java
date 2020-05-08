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
public class OwnerSeesTrainersPersonalInfoStepDefinitions extends AbstractStep{

	@LocalServerPort
	private int port;
	
	@When ("I go to the trainers {string} personal information page")
	public void i_go_to_the_trainers_personal_information_page(String trainer) {
	
		getDriver().findElement(By.linkText("Trainers")).click();
		getDriver().findElement(By.xpath("//table[@id='trainersTable']/tbody/tr/td")).click();
		assertEquals(trainer, getDriver().findElement(By.xpath("//table[@id='trainersTable']/tbody/tr/td")).getText());
			
	}

	@Then ("I can see this trainers name {string} and his email {string}")
	public void i_can_see_this_trainers_name_and_his_email (String name, String email){
		getDriver().findElement(By.linkText("John Doe")).click();
		getDriver().findElement(By.xpath("//b")).click();
		getDriver().findElement(By.xpath("//tr[2]/td")).click();
		getDriver().findElement(By.xpath("//tr[3]/td")).click();
	    assertEquals(name, getDriver().findElement(By.xpath("//b")).getText());
	    assertEquals(email, getDriver().findElement(By.xpath("//tr[2]/td")).getText());
			
	}
}
