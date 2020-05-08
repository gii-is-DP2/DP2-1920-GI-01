
package org.springframework.samples.petclinic.bdd.stepdefinitions;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
public class VetDeletesMedicalRecordStepDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;


	@When("I try to delete a medical record")
	public void i_try_to_delete_a_medical_record() {
		new WebDriverWait(this.getDriver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.dropdown-toggle")));
		this.getDriver().get("http://localhost:" + this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		this.getDriver().findElement(By.linkText("Jean Coleman")).click();
		this.getDriver().findElement(By.xpath("(//a[contains(text(),'Medical History')])[2]")).click();
		this.getDriver().findElement(By.linkText("2013-01-01")).click();
		this.getDriver().findElement(By.linkText("Delete")).click();
	}

	@Then("it is deleted and it is not shown in the list")
	public void it_is_deleted() {
		WebElement table = this.getDriver().findElement(By.xpath("//table[@id='medicalRecordsTable']/tbody"));
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		int rowsCountBefore = rows.size();

		this.getDriver().findElement(By.linkText("2013-01-01")).click();
		this.getDriver().findElement(By.linkText("Delete")).click();

		table = this.getDriver().findElement(By.xpath("//table[@id='medicalRecordsTable']/tbody"));
		rows = table.findElements(By.tagName("tr"));
		int rowsCountAfter = rows.size();
		Assert.assertEquals(rowsCountBefore, rowsCountAfter + 1);
		this.stopDriver();
	}

	@Then("an error page appears")
	public void an_error_appears_in_the_delete() {
		WebElement table = this.getDriver().findElement(By.xpath("//table[@id='medicalRecordsTable']/tbody"));
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		int rowsCountBefore = rows.size();

		this.getDriver().get("http://localhost:" + this.port + "/owners/6/pets/7/visits/1/medical-record/delete?id=90");
		Assert.assertEquals("Something happened...", this.getDriver().findElement(By.xpath("//h2")).getText());

		this.getDriver().get("http://localhost:" + this.port);
		this.getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		this.getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		this.getDriver().findElement(By.linkText("Jean Coleman")).click();
		this.getDriver().findElement(By.xpath("(//a[contains(text(),'Medical History')])[2]")).click();

		table = this.getDriver().findElement(By.xpath("//table[@id='medicalRecordsTable']/tbody"));
		rows = table.findElements(By.tagName("tr"));
		int rowsCountAfter = rows.size();
		Assert.assertEquals(rowsCountBefore, rowsCountAfter);
		this.stopDriver();
	}

}
