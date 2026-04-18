package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Random;

public class P02_ProductsPage extends PageBase {

    public P02_ProductsPage(WebDriver driver) {
        super(driver);
    }

    // ─── Locators ─────────────────────────────────────────────────────────────
    private final By addToCartButtons = By.xpath("//button[text()='Add to cart']");
    private final By cartButton       = By.id("shopping_cart_container");
    private final By cartBadge        = By.cssSelector("[data-test='shopping-cart-badge']");
    private final By checkoutButton   = By.id("checkout");
    private final By itemPrices       = By.className("inventory_item_price");

    // ─── Add random products & return COUNT (not price) ───────────────────────
    public int addRandomProductsToCart() {
        shortWait(driver).until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(addToCartButtons));

        List<WebElement> buttons = driver.findElements(addToCartButtons);
        int maxProducts      = Math.min(buttons.size(), 6);
        int productsToAdd    = new Random().nextInt(maxProducts) + 1; // at least 1

        for (int i = 0; i < productsToAdd; i++) {
            // Re-fetch every iteration to avoid StaleElementReferenceException
            buttons = driver.findElements(addToCartButtons);
            int randomIndex = new Random().nextInt(buttons.size());
            buttons.get(randomIndex).click();
        }

        return productsToAdd; // ← returns COUNT, not price
    }

    // ─── Add random products & return accumulated PRICE ───────────────────────
    public double addRandomProductsToCartAndGetPrice() {
        shortWait(driver).until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(addToCartButtons));

        List<WebElement> buttons = driver.findElements(addToCartButtons);
        List<WebElement> prices  = driver.findElements(itemPrices);

        int maxProducts   = Math.min(buttons.size(), 6);
        int productsToAdd = new Random().nextInt(maxProducts) + 1;
        double total      = 0.0;

        for (int i = 0; i < productsToAdd; i++) {
            buttons = driver.findElements(addToCartButtons);
            prices  = driver.findElements(itemPrices);

            int randomIndex = new Random().nextInt(buttons.size());
            String priceText = prices.get(randomIndex).getText().replaceAll("[^\\d.]", "");
            total += Double.parseDouble(priceText);

            buttons.get(randomIndex).click();
        }

        return total;
    }

    // ─── Navigation ───────────────────────────────────────────────────────────
    public P02_ProductsPage clickOnCartIcon() {
        shortWait(driver).until(ExpectedConditions.elementToBeClickable(cartButton)).click();
        return this;
    }

    public P02_ProductsPage clickOnCheckoutButton() {
        shortWait(driver).until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
        return this;
    }

    // ─── Cart badge ───────────────────────────────────────────────────────────
    public String getCartBadgeText() {
        List<WebElement> badge = driver.findElements(cartBadge);
        return !badge.isEmpty() ? badge.get(0).getText() : "0";
    }
}
