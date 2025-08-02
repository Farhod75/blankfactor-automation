package com.automation.pages;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class BlankFactorRetirementTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;
    private JavascriptExecutor js;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        actions = new Actions(driver);
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void testRetirementTileFlip() throws InterruptedException {
        driver.get("https://blankfactor.com/industries/retirement-and-wealth/");
        Thread.sleep(3000);

        // Accept cookies if present
        try {
            WebElement cookieBtn = driver.findElement(By.xpath("//button[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'accept')]"));
            if (cookieBtn.isDisplayed()) {
                cookieBtn.click();
                Thread.sleep(1000);
            }
        } catch (Exception ignored) {}

        String backText = getRetirementTileBackText();
        Assert.assertTrue(backText.toLowerCase().contains("machine learning"), "Back side text does not mention 'machine learning'");
    }

    public String getRetirementTileBackText() throws InterruptedException {
        // Find the main container for the tiles
        WebElement mainContainer = driver.findElement(By.xpath("//div[contains(@class, 'cards-grid-slider')]"));

        // Find all tiles (swiper-slide divs) inside the main container
        List<WebElement> tiles = mainContainer.findElements(By.xpath(".//div[contains(@class, 'swiper-slide')]"));
        System.out.println("Tiles found: " + tiles.size());
        Assert.assertTrue(tiles.size() >= 3, "Less than 3 tiles found!");

        // Hover over the third tile to flip it
        WebElement thirdTile = tiles.get(2);
        actions.moveToElement(thirdTile).perform();
        Thread.sleep(1500);

        // Get the text from the back side of the flip card
        WebElement backSide = thirdTile.findElement(By.xpath(".//div[contains(@class, 'flip-card-back')]//div[contains(@class, 'card-text')]"));
        System.out.println("Back side text: " + backSide.getText());

        return backSide.getText();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}