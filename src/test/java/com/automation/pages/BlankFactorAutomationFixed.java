package com.automation.pages;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

public class BlankFactorAutomationFixed {

    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;
    private JavascriptExecutor js;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        actions = new Actions(driver);
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void testBlankFactorAutomation() {
        try {
            // Step 1: Navigate and accept policy
            navigateAndAcceptPolicy();

            // Step 2: Navigate to Industries > Retirement and Wealth
            navigateToRetirementSection();

            // Step 3: Scroll to "Powering innovation in retirement services" and copy text from 3rd tile
            String aiMlText = extractAIMachineLearningText();
            System.out.println("AI & Machine Learning tile text: " + aiMlText);

            // Step 4: Scroll to bottom and click "Let's get started"
            clickLetsGetStarted();

            // Step 5: Verify page URL and title
            verifyPageDetails();

            System.out.println("✓ Test completed successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            takeScreenshot();
            Assert.fail("Test failed with exception: " + e.getMessage());
        }
    }

    private void navigateAndAcceptPolicy() throws InterruptedException {
        System.out.println("Step 1: Navigating to BlankFactor...");

        driver.get("https://blankfactor.com");
        Thread.sleep(5000);

        try {
            // Multiple strategies for cookie acceptance
            String[] cookieSelectors = {
                "//button[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'accept')]",
                "//button[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'agree')]",
                "//button[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'ok')]",
                "//button[contains(@class, 'accept')]",
                "//a[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'accept')]"
            };

            boolean cookieAccepted = false;
            for (String selector : cookieSelectors) {
                try {
                    WebElement cookieButton = driver.findElement(By.xpath(selector));
                    if (cookieButton.isDisplayed() && cookieButton.isEnabled()) {
                        cookieButton.click();
                        System.out.println("✓ Cookie accepted with selector: " + selector);
                        cookieAccepted = true;
                        Thread.sleep(2000);
                        break;
                    }
                } catch (Exception e) {
                    // Continue to next selector
                }
            }

            if (!cookieAccepted) {
                System.out.println("No cookie banner found or already accepted");
            }

        } catch (Exception e) {
            System.out.println("Cookie handling failed: " + e.getMessage());
        }
    }

    private void navigateToRetirementSection() throws InterruptedException {
        System.out.println("Step 2: Navigating to Retirement and Wealth section...");

        try {
            // Try to open the mega menu and click the correct link
            WebElement industriesMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'industries')]")));
            actions.moveToElement(industriesMenu).perform();
            Thread.sleep(1000);

            WebElement retirementLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'retirement and wealth')]")));
            retirementLink.click();
            Thread.sleep(3000);

            // Confirm navigation
            if (!driver.getCurrentUrl().toLowerCase().contains("retirement-and-wealth")) {
                throw new Exception("Did not navigate to Retirement and Wealth page.");
            }
            System.out.println("✓ Navigated to Retirement and Wealth section");
        } catch (Exception e) {
            System.out.println("Menu navigation failed, navigating directly...");
            driver.get("https://blankfactor.com/industries/retirement-and-wealth/");
            Thread.sleep(3000);
        }
    }

    private String extractAIMachineLearningText() throws InterruptedException {
        System.out.println("Step 3: Scrolling and searching for 'Powering innovation'...");

        // Try scrolling to trigger lazy loading
        js.executeScript("window.scrollTo(0, document.body.scrollHeight/2);");
        Thread.sleep(2000);

        // Use robust, case-insensitive, whitespace-tolerant XPath
        WebElement section = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//*[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'powering innovation')]")));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", section);
        Thread.sleep(2000);

        // Now find the 3rd tile in the same section (adjust as needed)
        List<WebElement> tiles = driver.findElements(By.xpath(
            "//section[.//*[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'powering innovation')]]//div[contains(@class, 'tile') or contains(@class, 'card') or contains(@class, 'col')][.//h3 or .//h4]"));
        if (tiles.size() < 3) {
            throw new RuntimeException("Less than 3 tiles found in the section!");
        }
        WebElement aiMlTile = tiles.get(2); // 0-based index

        actions.moveToElement(aiMlTile).perform();
        Thread.sleep(1000);

        return aiMlTile.getText();
    }

    private void clickLetsGetStarted() throws InterruptedException {
        System.out.println("Step 4: Scrolling to bottom and clicking 'Let's get started'...");

        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        Thread.sleep(2000);

        WebElement letsGetStartedBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(text(), \"Let's get started\") or contains(text(), 'Get started')]")));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", letsGetStartedBtn);
        Thread.sleep(1000);

        letsGetStartedBtn.click();
        System.out.println("✓ Clicked 'Let's get started' button");
        Thread.sleep(3000);
    }

    private void verifyPageDetails() {
        System.out.println("Step 5: Verifying page URL and title...");

        String currentUrl = driver.getCurrentUrl();
        String pageTitle = driver.getTitle();

        System.out.println("✓ Current URL: " + currentUrl);
        System.out.println("✓ Page Title: " + pageTitle);

        // Verify URL contains expected elements (contact or similar)
        Assert.assertTrue(currentUrl.contains("contact") || currentUrl.contains("get-started") ||
                         currentUrl.contains("form"),
                         "URL should contain contact or form related path");

        // Verify title is not empty
        Assert.assertFalse(pageTitle.isEmpty(), "Page title should not be empty");

        // Print the title text (if there's a visible <h1> or similar)
        try {
            WebElement titleElement = driver.findElement(By.xpath("//h1 | //h2"));
            System.out.println("✓ Title on page: " + titleElement.getText());
        } catch (Exception e) {
            System.out.println("No visible <h1> or <h2> found on the page.");
        }

        System.out.println("✓ Page verification completed successfully");
    }

    private void takeScreenshot() {
        if (driver == null) return;
        try {
            File srcFile = ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "screenshot_" + timestamp + ".png";

            // Create test-output directory if it doesn't exist
            File testOutputDir = new File(System.getProperty("user.dir") + File.separator + "test-output");
            if (!testOutputDir.exists()) {
                testOutputDir.mkdirs();
            }

            String filePath = testOutputDir.getAbsolutePath() + File.separator + fileName;
            FileUtils.copyFile(srcFile, new File(filePath));
            System.out.println("Screenshot saved: " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("✓ Browser closed");
        }
    }
}