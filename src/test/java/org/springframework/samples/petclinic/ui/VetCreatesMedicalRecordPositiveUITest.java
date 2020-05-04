
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
public class VetCreatesMedicalRecordPositiveUITest {

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
	public void testVetShouldCreateMedicalRecord() throws Exception {

		this.driver.manage().window().maximize();

		VetCreatesMedicalRecordPositiveUITest.loginAsVet(this.driver, this.port);

		VetCreatesMedicalRecordPositiveUITest.testMedicalRecordCreatePositive(this.driver, this.port);

		VetCreatesMedicalRecordPositiveUITest.checkIsCorrect(this.driver);

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

	public static void testMedicalRecordCreatePositive(final WebDriver driver, final int port) {
		driver.get("http://localhost:" + port);
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.linkText("Jean Coleman")).click();
		driver.findElement(By.xpath("(//a[contains(text(),'Add Medical Record')])[3]")).click();
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("La herida progresa adecuadamente");
		driver.findElement(By.id("status")).click();
		driver.findElement(By.id("status")).clear();
		driver.findElement(By.id("status")).sendKeys("Sano");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	public static void checkIsCorrect(final WebDriver driver) {
		Assert.assertEquals("Sano", driver.findElement(By.xpath("//tr[2]/td")).getText());
		Assert.assertEquals("La herida progresa adecuadamente", driver.findElement(By.xpath("//b")).getText());
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
