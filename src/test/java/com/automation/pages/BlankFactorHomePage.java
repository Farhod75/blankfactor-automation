package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;

public class BlankFactorHomePage {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;
    private JavascriptExecutor js;
    
    @FindBy(xpath = "//button[contains(text(), 'Accept') or contains(text(), 'OK')]")
    private WebElement acceptPolicyButton;
    
    @FindBy(xpath = "//a[contains(text(), 'Industries')]")
    private WebElement industriesMenu;
    
    public BlankFactorHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }
    
    public void acceptPolicy() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(acceptPolicyButton));
            acceptPolicyButton.click();
        } catch (Exception e) {
            System.out.println("No policy popup found");
        }
    }
    
    public void hoverIndustriesMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(industriesMenu));
        actions.moveToElement(industriesMenu).perform();
    }
}