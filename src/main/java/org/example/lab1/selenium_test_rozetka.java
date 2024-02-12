package org.example.lab1;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class selenium_test_rozetka {
    private WebDriver firefoxDriver;

    private static final String baseUrl = "https://rozetka.com.ua/ua/";

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--start-fullscreen");
        firefoxOptions.setImplicitWaitTimeout(Duration.ofSeconds(25));
        this.firefoxDriver = new FirefoxDriver(firefoxOptions);
    }

    @BeforeMethod
    public void precondition() {
        firefoxDriver.get(baseUrl);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        firefoxDriver.quit();
    }

    @Test
    public void testClick() {
        WebElement menuBtn = firefoxDriver.findElement(By.xpath("/html/body/app-root/div/div/rz-header/rz-main-header/header/div/div/rz-mobile-user-menu/button"));
        Assert.assertNotNull(menuBtn);
        menuBtn.click();
    }

    @Test
    public void testSearchField() {

        WebElement searchField = firefoxDriver.findElement(By.tagName("input"));
        Assert.assertNotNull(searchField);
        String inputValue = "HDMI";
        searchField.sendKeys(inputValue);
        Assert.assertEquals(searchField.getAttribute("value"), inputValue);
        searchField.sendKeys(Keys.ENTER);
        WebDriverWait wait = new WebDriverWait(firefoxDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(baseUrl)));
        Assert.assertNotEquals(firefoxDriver.getCurrentUrl(), baseUrl);

    }
    @Test
    public void testLangBtn() {

        for(int i = 0; i < 5; i++){

            WebElement ruLangBtn = firefoxDriver.findElement(By.xpath("/html/body/app-root/div/div/rz-header/rz-main-header/header/div/div/ul/li[1]/rz-lang/ul/li[1]"));
            Assert.assertNotNull(ruLangBtn);

            WebElement ukLangBtn = firefoxDriver.findElement(By.xpath("/html/body/app-root/div/div/rz-header/rz-main-header/header/div/div/ul/li[1]/rz-lang/ul/li[2]"));
            Assert.assertNotNull(ukLangBtn);
            if(ruLangBtn.getAttribute("class").contains("active")){
               ukLangBtn.click();
               WebDriverWait wait = new WebDriverWait(firefoxDriver, Duration.ofSeconds(10));
               wait.until(ExpectedConditions.urlToBe(baseUrl));
           }
           else {
               ruLangBtn.click();
               WebDriverWait wait = new WebDriverWait(firefoxDriver, Duration.ofSeconds(10));
               wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(baseUrl)));
           }
        }
    }

}
