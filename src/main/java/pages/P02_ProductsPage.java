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

    // ─── Each product item container ──────────────────────────────────────────
    // بنجيب كل inventory item كـ container واحد عشان السعر والـ button يكونوا من نفس المنتج
    private final By inventoryItems   = By.className("inventory_item");

    // ─── Add random products & return COUNT ───────────────────────────────────
    public int addRandomProductsToCart() {
        shortWait(driver).until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(addToCartButtons));

        List<WebElement> buttons = driver.findElements(addToCartButtons);
        int productsToAdd = new Random().nextInt(Math.min(buttons.size(), 6)) + 1;

        for (int i = 0; i < productsToAdd; i++) {
            buttons = driver.findElements(addToCartButtons);
            buttons.get(new Random().nextInt(buttons.size())).click();
        }

        return productsToAdd;
    }

    // ─── Add random products & return accumulated PRICE (fixed) ───────────────
    public double addRandomProductsToCartAndGetPrice() {
        shortWait(driver).until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(inventoryItems));

        List<WebElement> items = driver.findElements(inventoryItems);
        int productsToAdd = new Random().nextInt(Math.min(items.size(), 6)) + 1;
        double total = 0.0;

        for (int i = 0; i < productsToAdd; i++) {
            // ── Re-fetch items every iteration ────────────────────────────────
            items = driver.findElements(inventoryItems);

            // ── Pick a random item that hasn't been added yet ─────────────────
            List<WebElement> availableItems = items.stream()
                    .filter(item -> !item.findElements(By.xpath(".//button[text()='Add to cart']")).isEmpty())
                    .toList();

            if (availableItems.isEmpty()) break;

            WebElement selectedItem = availableItems.get(new Random().nextInt(availableItems.size()));

            // ── Get price FROM THE SAME item container ─────────────────────────
            String priceText = selectedItem
                    .findElement(By.className("inventory_item_price"))
                    .getText()
                    .replaceAll("[^\\d.]", "");
            total += Double.parseDouble(priceText);

            // ── Click "Add to cart" inside the same container ──────────────────
            selectedItem.findElement(By.xpath(".//button[text()='Add to cart']")).click();
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
