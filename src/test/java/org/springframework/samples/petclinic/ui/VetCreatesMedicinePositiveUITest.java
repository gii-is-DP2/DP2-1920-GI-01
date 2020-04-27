package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class VetCreatesMedicinePositiveUITest {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @BeforeEach
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://www.google.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testUntitledTestCase() throws Exception {
    driver.get("http://localhost:8080/");
    driver.findElement(By.linkText("Login")).click();
    driver.findElement(By.id("username")).click();
    driver.findElement(By.id("username")).clear();
    driver.findElement(By.id("username")).sendKeys("vet1");
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys("v3t1");
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    driver.findElement(By.linkText("Medicines")).click();
    driver.findElement(By.linkText("Add Medicine")).click();
    driver.findElement(By.id("name")).click();
    driver.findElement(By.id("name")).clear();
    driver.findElement(By.id("name")).sendKeys("prueba");
    driver.findElement(By.id("expirationDate")).click();
    driver.findElement(By.linkText("25")).click();
    driver.findElement(By.id("maker")).click();
    driver.findElement(By.id("maker")).click();
    driver.findElement(By.id("maker")).clear();
    driver.findElement(By.id("maker")).sendKeys("prueba");
    new Select(driver.findElement(By.id("petType"))).selectByVisibleText("hamster");
    driver.findElement(By.xpath("//option[@value='hamster']")).click();
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    assertEquals("prueba", driver.findElement(By.xpath("//b")).getText());
    assertEquals("2020-04-25", driver.findElement(By.xpath("//tr[2]/td")).getText());
    assertEquals("prueba", driver.findElement(By.xpath("//tr[3]/td/b")).getText());
    assertEquals("hamster", driver.findElement(By.xpath("//tr[4]/td/b")).getText());
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
