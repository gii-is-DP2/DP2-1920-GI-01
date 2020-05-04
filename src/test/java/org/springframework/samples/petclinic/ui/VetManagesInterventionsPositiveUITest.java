
package org.springframework.samples.petclinic.ui;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VetManagesInterventionsPositiveUITest {

	private WebDriver		driver;
	private String			baseUrl;
	private boolean			acceptNextAlert		= true;
	private StringBuffer	verificationErrors	= new StringBuffer();

	@LocalServerPort
	private int				port;


	@BeforeEach
	public void setUp() throws Exception {
		String pathToGeckoDriver = "./src/test/resources/geckodriver.exe";
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver);
		this.driver = new FirefoxDriver();
		this.baseUrl = "https://www.google.com/";
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testVetShouldAddNewIntervention() throws Exception {

		this.driver.manage().window().maximize();

		VetManagesInterventionsPositiveUITest.loginAsVet(this.driver, this.port);

		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		this.driver.findElement(By.name("lastName")).click();
		this.driver.findElement(By.name("lastName")).clear();
		this.driver.findElement(By.name("lastName")).sendKeys("Franklin");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.linkText("Add Intervention")).click();
		this.driver.findElement(By.id("interventionTime")).click();
		this.driver.findElement(By.id("interventionTime")).clear();
		this.driver.findElement(By.id("interventionTime")).sendKeys("6");
		this.driver.findElement(By.id("interventionDescription")).click();
		this.driver.findElement(By.id("interventionDescription")).clear();
		this.driver.findElement(By.id("interventionDescription")).sendKeys("Vet adds intervention positive UI Test");
		this.driver.findElement(By.id("interventionDate")).click();
		this.driver.findElement(By.id("interventionDate")).clear();

		// The intervention date bellow may provoke a failure if the test is runned after the date provided, if so, change the date to a future date

		this.driver.findElement(By.id("interventionDate")).sendKeys("2020/05/20");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	public static void loginAsVet(final WebDriver driver, final int port) {
		driver.get("http://localhost:" + port);
		new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Login')]")));
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		WebDriverWait wait = new WebDriverWait(driver, 200);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='submit']")));
		WebElement usernameInput = driver.findElement(By.xpath("//input[@id='username']"));
		usernameInput.clear();
		usernameInput.click();
		wait.until(ExpectedConditions.visibilityOf(usernameInput));
		usernameInput.sendKeys("vet1");
		WebElement passwordInput = driver.findElement(By.xpath("//input[@id='password']"));
		passwordInput.clear();
		passwordInput.click();
		wait.until(ExpectedConditions.visibilityOf(passwordInput));
		passwordInput.sendKeys("v3t1");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='submit']")));
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.driver.quit();
		String verificationErrorString = this.verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assert.fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(final By by) {
		try {
			this.driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			this.driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = this.driver.switchTo().alert();
			String alertText = alert.getText();
			if (this.acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			this.acceptNextAlert = true;
		}
	}

}
