package Testcases;

import io.qameta.allure.*;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.P02_ProductsPage;
import retrytests.RetryAnalyzer;

import java.io.IOException;

import static drivers.DriverHolder.getDriver;

@Epic("Shopping Cart")
@Story("Cart Badge Story")
public class TC02_Products extends TestBase {

    @Feature("Add to Cart")
    @Description("Verify that the cart badge reflects the correct number of added products")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Verify cart badge reflects number of added products",
            retryAnalyzer = RetryAnalyzer.class)
    public void verifyCartBadgeReflectsAddedProducts() throws IOException, ParseException {

        // ── Step 1: Login ──────────────────────────────────────────────────────
        loginWithValidCredentials();

        // ── Step 2: Add random products & get COUNT ────────────────────────────
        P02_ProductsPage productsPage = new P02_ProductsPage(getDriver());
        int expectedCount = productsPage.addRandomProductsToCart();

        log.info("Products added to cart: " + expectedCount);

        // ── Step 3: Assert badge = count ──────────────────────────────────────
        String actualBadgeText = productsPage.getCartBadgeText();
        Assert.assertEquals(
                actualBadgeText,
                String.valueOf(expectedCount),
                "ERROR: Cart badge does not match number of products added!");

        // ── Step 4: Assert still on inventory page ────────────────────────────
        Assert.assertEquals(
                getDriver().getCurrentUrl(),
                "https://www.saucedemo.com/inventory.html",
                "ERROR: Unexpected page after adding products.");

        log.info("TC02 passed — badge shows: " + actualBadgeText);
    }
}
