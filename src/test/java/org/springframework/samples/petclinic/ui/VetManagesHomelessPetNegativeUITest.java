package org.springframework.samples.petclinic.ui;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
 class VetManagesHomelessPetNegativeUITest {

	  private WebDriver driver;
	  private String baseUrl;
	  private boolean acceptNextAlert = true;
	  private StringBuffer verificationErrors = new StringBuffer();

	  @LocalServerPort
	  private int port;
	  
	  @BeforeEach
	  public void setUp() throws Exception {
		String pathToGeckoDriver = "./src/test/resources/geckodriver.exe";
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver);
		driver = new FirefoxDriver();
	    baseUrl = "https://www.google.com/";
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	  }

	  @Test
	  void testVetShouldNotAddNewPet() throws Exception {
		  
		driver.manage().window().maximize();
		
		loginAsVet(driver, port);

		goToForm(driver);
		
		fillTheForm(driver);

	    try {
	      assertEquals("required and before current date", driver.findElement(By.xpath("//form[@id='pet']/div/div[2]/div/span[2]")).getText());
	    } catch (Error e) {
	      verificationErrors.append(e.toString());
	    }
	  }
	  
	  public static void loginAsVet(WebDriver driver, int port) {
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
	  
	  public static void goToForm(WebDriver driver) {
		  new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.dropdown-toggle")));
		  driver.findElement(By.cssSelector("a.dropdown-toggle")).click();
		  driver.findElement(By.xpath("//a[contains(text(),'Manage homeless pets')]")).click();
		  driver.findElement(By.xpath("//a[contains(text(),'Add a Pet')]")).click();
	  }
	  
	  public static void fillTheForm(WebDriver driver) {
		  driver.findElement(By.id("name")).click();
		  driver.findElement(By.id("name")).clear();
		  driver.findElement(By.id("name")).sendKeys("Ernie");
		  driver.findElement(By.id("birthDate")).click();
		  driver.findElement(By.id("birthDate")).clear();
		  driver.findElement(By.id("birthDate")).sendKeys("2200/02/20");
		  driver.findElement(By.linkText("20")).click();
		  new Select(driver.findElement(By.id("type"))).selectByVisibleText("hamster");
		  driver.findElement(By.xpath("//option[@value='hamster']")).click();
		  driver.findElement(By.xpath("//button[@type='submit']")).click();
	  }

	  @AfterEach
	  public void tearDown() throws Exception {
	    driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
	  }

	  private boolean isElementPresent(By by) {
	    try {
	      driver.findElement(by);
	      return true;
	    } catch (NoSuchElementException e) {
	      return false;
	    }
	  }

	  private boolean isAlertPresent() {
	    try {
	      driver.switchTo().alert();
	      return true;
	    } catch (NoAlertPresentException e) {
	      return false;
	    }
	  }

	  private String closeAlertAndGetItsText() {
	    try {
	      Alert alert = driver.switchTo().alert();
	      String alertText = alert.getText();
	      if (acceptNextAlert) {
	        alert.accept();
	      } else {
	        alert.dismiss();
	      }
	      return alertText;
	    } finally {
	      acceptNextAlert = true;
	    }
	  }
	
}
