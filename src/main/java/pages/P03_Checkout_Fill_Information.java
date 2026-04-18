package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static utli.Utility.generateRandomName;
import static utli.Utility.getRandomZipCodePrefix;

public class P03_Checkout_Fill_Information extends PageBase {

    public P03_Checkout_Fill_Information(WebDriver driver) {
        super(driver);
    }

    // ─── Locators ─────────────────────────────────────────────────────────────
    private final By FIRST_NAME       = By.id("first-name");
    private final By LAST_NAME        = By.id("last-name");
    private final By ZIP              = By.id("postal-code");
    private final By CONTINUE_BUTTON  = By.xpath("//input[@value='Continue']"); // SauceDemo uses "Continue" not "CONTINUE"

    // ─── Random test data (generated once per page object instance) ───────────
    private final String firstNameR = generateRandomName(1);
    private final String lastNameR  = generateRandomName(1);
    private final String zipCode    = getRandomZipCodePrefix();

    // ─── Actions ──────────────────────────────────────────────────────────────
    public P03_Checkout_Fill_Information EnterFirstName() {
        shortWait(driver).until(ExpectedConditions.elementToBeClickable(FIRST_NAME));
        driver.findElement(FIRST_NAME).clear();
        driver.findElement(FIRST_NAME).sendKeys(firstNameR);
        return this;
    }

    public P03_Checkout_Fill_Information EnterLastName() {
        shortWait(driver).until(ExpectedConditions.elementToBeClickable(LAST_NAME));
        driver.findElement(LAST_NAME).clear();
        driver.findElement(LAST_NAME).sendKeys(lastNameR);
        return this;
    }

    public P03_Checkout_Fill_Information EnterZIPCode() {
        shortWait(driver).until(ExpectedConditions.elementToBeClickable(ZIP));
        driver.findElement(ZIP).clear();
        driver.findElement(ZIP).sendKeys(zipCode);
        return this;
    }

    public P03_Checkout_Fill_Information ClickOnContinue() {
        shortWait(driver).until(ExpectedConditions.elementToBeClickable(CONTINUE_BUTTON));
        driver.findElement(CONTINUE_BUTTON).click();
        return this;
    }
}
