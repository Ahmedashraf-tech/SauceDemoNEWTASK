package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class P01_LoginPage extends PageBase {

    // ─── Locators ─────────────────────────────────────────────────────────────
    private final By USERNAME      = By.id("user-name");
    private final By PASSWORD      = By.id("password");
    private final By LOGIN_BUTTON  = By.id("login-button");
    private final By ERROR_HEADING = By.cssSelector("h3[data-test='error']");

    // ─── Constructor ──────────────────────────────────────────────────────────
    public P01_LoginPage(WebDriver driver) {
        super(driver);   // ← calls PageBase(driver) → sets this.driver correctly
    }

    // ─── Page Actions ─────────────────────────────────────────────────────────

    public P01_LoginPage EnterUserName(String username) {
        shortWait(driver).until(ExpectedConditions.elementToBeClickable(USERNAME));
        driver.findElement(USERNAME).clear();
        driver.findElement(USERNAME).sendKeys(username);
        return this;
    }

    public P01_LoginPage EnterPassword(String password) {
        shortWait(driver).until(ExpectedConditions.elementToBeClickable(PASSWORD));
        driver.findElement(PASSWORD).clear();
        driver.findElement(PASSWORD).sendKeys(password);
        return this;
    }

    public P01_LoginPage ClickonLoginButton() {
        shortWait(driver).until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON));
        driver.findElement(LOGIN_BUTTON).click();
        return this;
    }

    public String getErrorMessage() {
        shortWait(driver).until(ExpectedConditions.visibilityOfElementLocated(ERROR_HEADING));
        return driver.findElement(ERROR_HEADING).getText();
    }
}
