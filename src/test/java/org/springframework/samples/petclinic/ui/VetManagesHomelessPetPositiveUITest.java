package org.springframework.samples.petclinic.ui;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VetManagesHomelessPetPositiveUITest {

	  private WebDriver driver;
	  private String baseUrl;
	  private boolean acceptNextAlert = true;
	  private StringBuffer verificationErrors = new StringBuffer();
	  
	  @LocalServerPort
	  private int port;

	  @BeforeEach
	  public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", System.getenv("webdriver.chrome.driver"));
	    driver = new ChromeDriver();
	    baseUrl = "https://www.google.com/";
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	  }

	  @Test
	  public void testVetShouldAddNewPet() throws Exception {
		  
		driver.manage().window().maximize();

		loginAsVet(driver, port);

		new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.dropdown-toggle")));
		//new WebDriverWait(driver, 100).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@href, '#')]")));
	    driver.findElement(By.cssSelector("a.dropdown-toggle")).click();
	    driver.findElement(By.xpath("//a[contains(text(),'Manage homeless pets')]")).click();
	    //new WebDriverWait(driver, 100).until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Add a Pet')]")));
	    driver.findElement(By.xpath("//a[contains(text(),'Add a Pet')]")).click();
	    driver.findElement(By.id("name")).click();
	    driver.findElement(By.id("name")).clear();
	    driver.findElement(By.id("name")).sendKeys("Ernie");
	    driver.findElement(By.id("birthDate")).click();
	    driver.findElement(By.id("birthDate")).clear();
	    driver.findElement(By.id("birthDate")).sendKeys("2020/01/01");
	    driver.findElement(By.xpath("(//a[contains(@href, '#')])[2]")).click();
	    new Select(driver.findElement(By.id("type"))).selectByVisibleText("hamster");
	    driver.findElement(By.xpath("//option[@value='hamster']")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    try {
	      assertTrue(isElementPresent(By.xpath("//a[contains(text(),'Ernie')]")));
	    } catch (Error e) {
	      verificationErrors.append(e.toString());
	    }
	  }
	  
	  public static void loginAsVet(WebDriver driver, int port) {
		  
		  driver.get("http://localhost:" + port);
		  new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Login')]")));
		  driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		  //driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		  WebDriverWait wait = new WebDriverWait(driver, 200);
		  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='submit']")));
		  new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='username']")));
		  WebElement usernameInput = driver.findElement(By.xpath("//input[@id='username']"));
		  //driver.findElement(By.name("username")).clear();
		  //driver.findElement(By.name("username")).click();
		  //driver.findElement(By.name("username")).sendKeys("vet1");
		  usernameInput.clear();
		  usernameInput.click();
		  wait.until(ExpectedConditions.visibilityOf(usernameInput));
		  usernameInput.sendKeys("vet1");
		  new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='password']")));
		  WebElement passwordInput = driver.findElement(By.xpath("//input[@id='password']"));
//		  driver.findElement(By.xpath("//input[@id='password']")).clear();
//		  driver.findElement(By.xpath("//input[@id='password']")).click();
//		  driver.findElement(By.xpath("//input[@id='password']")).sendKeys("v3t1");
		  passwordInput.clear();
		  passwordInput.click();
		  wait.until(ExpectedConditions.visibilityOf(passwordInput));
		  passwordInput.sendKeys("v3t1");
		  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='submit']")));
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
