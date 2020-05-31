package org.springframework.samples.petclinic.ui;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
 class TrainerEditsRehabNegativeUITest {

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
	  void testTrainerAddsRehab () throws Exception {
		  
		  driver.manage().window().maximize();
		  loginAsTrainer(driver, port);
		  testTrainerTriesToEditRehab ();
	  }
	  
	  public void loginAsTrainer(WebDriver driver, int port) {
		    driver.get("http://localhost:" + port);
		    new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Login')]")));
			driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
			WebDriverWait wait = new WebDriverWait(driver, 200);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='submit']")));
			WebElement usernameInput = driver.findElement(By.xpath("//input[@id='username']"));
			usernameInput.clear();
			usernameInput.click();
			wait.until(ExpectedConditions.visibilityOf(usernameInput));
			usernameInput.sendKeys("trainer1");
			WebElement passwordInput = driver.findElement(By.xpath("//input[@id='password']"));
			passwordInput.clear();
			passwordInput.click();
			wait.until(ExpectedConditions.visibilityOf(passwordInput));
			passwordInput.sendKeys("tr41n3r");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='submit']")));
			driver.findElement(By.xpath("//button[@type='submit']")).click();		   
		  }

	  public void testTrainerTriesToEditRehab () {
		  driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		    driver.findElement(By.name("lastName")).click();
		    driver.findElement(By.name("lastName")).clear();
		    driver.findElement(By.name("lastName")).sendKeys("Rodriquez");
		    driver.findElement(By.xpath("//button[@type='submit']")).click();
		    driver.findElement(By.xpath("(//a[contains(text(),'Add Rehab')])[2]")).click();
		    driver.findElement(By.id("time")).click();
		    driver.findElement(By.id("time")).clear();
		    driver.findElement(By.id("time")).sendKeys("3");
		    driver.findElement(By.id("description")).click();
		    driver.findElement(By.id("description")).click();
		    driver.findElement(By.id("description")).clear();
		    driver.findElement(By.id("description")).sendKeys("test");
		    driver.findElement(By.xpath("//button[@type='submit']")).click();
		    driver.findElement(By.linkText("Edit Rehabilitation")).click();
		    driver.findElement(By.id("description")).click();
		    driver.findElement(By.id("description")).clear();
		    driver.findElement(By.id("description")).sendKeys("");
		    driver.findElement(By.xpath("//button[@type='submit']")).click();
		    driver.findElement(By.xpath("//form[@id='rehab']/div/div[3]/div")).click();
		    assertEquals("must not be empty", driver.findElement(By.xpath("//form[@id='rehab']/div/div[3]/div")).getText());		  
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
