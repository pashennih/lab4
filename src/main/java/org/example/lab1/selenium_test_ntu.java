package org.example.lab1;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class selenium_test_ntu {
    private WebDriver firefoxDriver;

    private static final String baseUrl = "https://www.nmu.org.ua/ua/";

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
    public void testHeaderExist() {
        WebElement header = firefoxDriver.findElement(By.id("nmul"));
        Assert.assertNotNull(header);
    }

    @Test
    public void testClickOnForStudent() {
        WebElement forStudentBtn = firefoxDriver.findElement(By.xpath("/html/body/center/div[4]/div/div[1]/ul/li[4]/a"));
        Assert.assertNotNull(forStudentBtn);
        forStudentBtn.click();
        Assert.assertNotEquals(firefoxDriver.getCurrentUrl(), baseUrl);
    }

    @Test
    public void testSearchFieldOnForStudentPage() {
        String studentPageUrl = "content/student_life/students/";
        firefoxDriver.get(baseUrl + studentPageUrl);

        WebElement searchField = firefoxDriver.findElement(By.tagName("input"));
        Assert.assertNotNull(searchField);

        System.out.println(
                String.format("Name attribute: %s", searchField.getAttribute("name")) +
                        String.format("\nID attribute: %s", searchField.getAttribute("id")) +
                        String.format("\nType attribute: %s", searchField.getAttribute("type")) +
                        String.format("\nValue attribute: %s", searchField.getAttribute("value")) +
                        String.format("\nPosition attribute: (%s, %s)", searchField.getLocation().x, searchField.getLocation().y) +
                        String.format("\nSize attribute: (%s, %s)", searchField.getSize().height, searchField.getSize().width)
        );

        searchField.sendKeys(Keys.ENTER);
        Assert.assertNotEquals(firefoxDriver.getCurrentUrl(), studentPageUrl);

        WebElement searchField2 = firefoxDriver.findElement(By.xpath("/html/body/center/div[4]/div/table/tbody/tr/td[2]/div/div/div/div/div/form/table/tbody/tr/td[1]/div/table/tbody/tr/td[1]/input"));
        Assert.assertNotNull(searchField2);

        String inputValue = "I need info";
        searchField2.sendKeys(inputValue);
        Assert.assertEquals(searchField2.getAttribute("value"), inputValue);

        searchField2.sendKeys(Keys.ENTER);
        Assert.assertNotEquals(firefoxDriver.getCurrentUrl(), studentPageUrl);
    }
    @Test
    public void testSlider() {
        WebElement nextButton = firefoxDriver.findElement(By.className("next"));
        WebElement nextButtonByCss = firefoxDriver.findElement(By.cssSelector("a.next"));
        Assert.assertEquals(nextButton, nextButtonByCss);

        WebElement previousBtn = firefoxDriver.findElement(By.className("prev"));

        for(int i = 0; i < 10; i++){
           if(nextButton.getAttribute("class").contains("disabled")){
               previousBtn.click();
               Assert.assertFalse(nextButton.getAttribute("class").contains("disabled"));
           }
           else {
               nextButton.click();
               Assert.assertFalse(previousBtn.getAttribute("class").contains("disabled"));
           }
        }
        for(int i = 0; i < 10; i++){
            if(previousBtn.getAttribute("class").contains("disabled")){
                nextButton.click();
                Assert.assertFalse(previousBtn.getAttribute("class").contains("disabled"));
            }
            else {
                previousBtn.click();
                Assert.assertFalse(nextButton.getAttribute("class").contains("disabled"));
            }
        }
    }
}
