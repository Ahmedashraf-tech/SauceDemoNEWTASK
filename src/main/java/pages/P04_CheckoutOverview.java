package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class P04_CheckoutOverview extends PageBase {

    public P04_CheckoutOverview(WebDriver driver) {
        super(driver);
    }

    // ─── Locators ─────────────────────────────────────────────────────────────
    private final By priceItems    = By.className("inventory_item_price");
    private final By totalLabel    = By.className("summary_subtotal_label");
    private final By finishButton  = By.id("finish");
    private final By successHeader = By.className("complete-header");

    // ─── Subtotal calculated from individual item prices ──────────────────────
    public double getCalculatedSubtotal() {
        shortWait(driver).until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(priceItems));
        double total = 0.0;
        for (var element : driver.findElements(priceItems)) {
            total += Double.parseDouble(element.getText().replaceAll("[^\\d.]", ""));
        }
        return total;
    }

    // ─── Subtotal shown in the summary label ──────────────────────────────────
    public double getDisplayedSubtotal() {
        shortWait(driver).until(
                ExpectedConditions.visibilityOfElementLocated(totalLabel));
        String text = driver.findElement(totalLabel).getText();
        return Double.parseDouble(text.replaceAll("[^\\d.]", ""));
    }

    // ─── Click finish button ──────────────────────────────────────────────────
    public P04_CheckoutOverview clickOnFinishButton() {
        shortWait(driver).until(
                ExpectedConditions.elementToBeClickable(finishButton)).click();
        return this;
    }

    // ─── Get success message text ─────────────────────────────────────────────
    public String getSuccessMessageText() {
        return shortWait(driver).until(
                ExpectedConditions.visibilityOfElementLocated(successHeader)).getText();
    }
}
