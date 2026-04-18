package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;

public class PageBase {

    protected WebDriver driver;

    // ─── Constructors ─────────────────────────────────────────────────────────
    public PageBase(WebDriver driver) {
        this.driver = driver;
    }

    public PageBase() {
    }

    // ─── Actions ──────────────────────────────────────────────────────────────

    /**
     * Hover over a WebElement using the driver passed in (no static DriverHolder dependency).
     */
    public static void hoverWebElement(WebDriver driver, WebElement element) {
        new Actions(driver).moveToElement(element).perform();
    }

    // ─── Waits ────────────────────────────────────────────────────────────────

    public static void explicitWait(WebDriver driver, By locator) {
        new WebDriverWait(driver, Duration.ofSeconds(50))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void fluentWaitHandling(WebDriver driver, By locator) {
        new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(50))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(Exception.class)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebDriverWait longWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    public static WebDriverWait shortWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ─── Browser Teardown ─────────────────────────────────────────────────────

    /**
     * Clears storage/cookies then quits the browser.
     * Skips JS clear if the driver is already dead (avoids WebDriverException on quit).
     */
    public static void quitBrowser(WebDriver driver) {
        if (driver == null) return;

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.localStorage.clear();");
            js.executeScript("window.sessionStorage.clear();");
            driver.manage().deleteAllCookies();
        } catch (WebDriverException e) {
            // Driver may already be unresponsive – safe to ignore before quit
            System.err.println("[quitBrowser] Could not clear storage: " + e.getMessage());
        }

        try {
            driver.quit();
        } catch (WebDriverException e) {
            System.err.println("[quitBrowser] driver.quit() failed: " + e.getMessage());
        }

        // Kill any stale chromedriver processes
        killDriverProcess();
    }

    private static void killDriverProcess() {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("win")) {
                Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
            } else if (os.contains("mac") || os.contains("nix") || os.contains("nux")) {
                Runtime.getRuntime().exec("pkill -f chromedriver");
                Runtime.getRuntime().exec("pkill -f chrome");
            }
        } catch (IOException e) {
            System.err.println("[killDriverProcess] Could not kill driver process: " + e.getMessage());
        }
    }
}
