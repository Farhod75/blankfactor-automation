package com.automation.pages;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class BlankFactorRetirementE2ETest {
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
    public void testEndToEndRetirementFlow() throws InterruptedException {
        // 1. Navigate to homepage and accept policy
        driver.get("https://blankfactor.com/");
        Thread.sleep(2000);
        try {
            WebElement cookieBtn = driver.findElement(By.xpath("//button[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'accept')]"));
            if (cookieBtn.isDisplayed()) {
                cookieBtn.click();
                Thread.sleep(1000);
            }
        } catch (Exception ignored) {}

        // 2. Navigate to Industries > Retirement and Wealth
        try {
            WebElement industriesMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'industries')]")));
            actions.moveToElement(industriesMenu).perform();
            Thread.sleep(1000);

            WebElement retirementLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'retirement and wealth')]")));
            retirementLink.click();
            Thread.sleep(3000);
        } catch (Exception e) {
            // Fallback: direct navigation
            driver.get("https://blankfactor.com/industries/retirement-and-wealth/");
            Thread.sleep(2000);
        }

        // 3. Scroll to “Powering innovation in retirement services” and hover 3rd tile
        WebElement mainContainer = driver.findElement(By.xpath("//div[contains(@class, 'cards-grid-slider')]"));
        List<WebElement> tiles = mainContainer.findElements(By.xpath(".//div[contains(@class, 'swiper-slide')]"));
        Assert.assertTrue(tiles.size() >= 3, "Less than 3 tiles found!");
        WebElement thirdTile = tiles.get(2);
        actions.moveToElement(thirdTile).perform();
        Thread.sleep(1500);
        WebElement backSide = thirdTile.findElement(By.xpath(".//div[contains(@class, 'flip-card-back')]//div[contains(@class, 'card-text')]"));
        String aiMlText = backSide.getText();
        System.out.println("AI & Machine Learning tile text: " + aiMlText);

        // 4. Scroll to bottom and click "Let's get started"
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        Thread.sleep(2000);
        WebElement letsGetStartedBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(text(), \"Let's get started\") or contains(text(), 'Get started')]")));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", letsGetStartedBtn);
        Thread.sleep(1000);
        letsGetStartedBtn.click();
     // Wait for the new page to load by waiting for a visible h1 or h2
        WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//h1 | //h2")
        ));

     // 5. Verify page URL and title
        String currentUrl = driver.getCurrentUrl();
        String pageTitle = driver.getTitle();
        System.out.println("Final URL: " + currentUrl);
        System.out.println("Final Title: " + pageTitle);
        Assert.assertTrue(currentUrl.contains("contact") || currentUrl.contains("get-started") || currentUrl.contains("form"),
                "URL should contain contact or form related path");
        Assert.assertFalse(pageTitle.isEmpty(), "Page title should not be empty");

    /*    // 6. Print the text from the title (h1 or h2)
        try {
            WebElement titleElement = driver.findElement(By.xpath("//h1 | //h2"));
            System.out.println("Page title text: " + titleElement.getText());
        } catch (Exception e) {
            System.out.println("No visible <h1> or <h2> found on the page.");
        }
    }*/
     // 6. Print the text from the title (h1 or h2)
        System.out.println("Page title text: " + titleElement.getText());
    }
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}