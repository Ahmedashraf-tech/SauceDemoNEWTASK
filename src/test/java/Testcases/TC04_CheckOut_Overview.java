package Testcases;

import io.qameta.allure.*;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.P02_ProductsPage;
import pages.P03_Checkout_Fill_Information;
import pages.P04_CheckoutOverview;
import retrytests.RetryAnalyzer;

import java.io.IOException;

import static drivers.DriverHolder.getDriver;

@Epic("Checkout")
@Story("Checkout Overview Story")
public class TC04_CheckOut_Overview extends TestBase {

    @Feature("Checkout Step Two")
    @Description("Verify displayed subtotal matches sum of added product prices, then place order and confirm success message")
    @Severity(SeverityLevel.BLOCKER)
    @Test(priority = 1,
            description = "Verify price comparison and order success message",
            retryAnalyzer = RetryAnalyzer.class)
    public void verifyPriceAndSuccessMessage() throws IOException, ParseException {

        // ── Step 1: Login ──────────────────────────────────────────────────────
        loginWithValidCredentials();

        // ── Step 2: Add random products & collect accumulated price ───────────
        P02_ProductsPage productsPage = new P02_ProductsPage(getDriver());
        double accumulatedPrice = productsPage.addRandomProductsToCartAndGetPrice();
        productsPage.clickOnCartIcon();

        // ── Step 3: Navigate to checkout step one ─────────────────────────────
        getDriver().get("https://www.saucedemo.com/checkout-step-one.html");

        // ── Step 4: Fill information & continue to step two ───────────────────
        new P03_Checkout_Fill_Information(getDriver())
                .EnterFirstName()
                .EnterLastName()
                .EnterZIPCode()
                .ClickOnContinue();

        // ── Step 5: Assert we are on checkout step two ────────────────────────
        Assert.assertEquals(
                getDriver().getCurrentUrl(),
                "https://www.saucedemo.com/checkout-step-two.html",
                "ERROR: Did not reach checkout overview page!");

        // ── Step 6: Get displayed subtotal ────────────────────────────────────
        P04_CheckoutOverview overviewPage = new P04_CheckoutOverview(getDriver());
        double displayedSubtotal = overviewPage.getDisplayedSubtotal();

        // ── Step 7: Display both values in Terminal ───────────────────────────
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║           PRICE COMPARISON REPORT         ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.printf( "║  Accumulated from Products Page : $%-6.2f ║%n", accumulatedPrice);
        System.out.printf( "║  Displayed Subtotal in Overview : $%-6.2f ║%n", displayedSubtotal);
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.printf( "║  Match : %-32s ║%n",
                (Math.abs(accumulatedPrice - displayedSubtotal) <= 0.01) ? "✔ PASS" : "✘ FAIL");
        System.out.println("╚══════════════════════════════════════════╝\n");

        // ── Step 8: Assert prices match ───────────────────────────────────────
        Assert.assertEquals(
                accumulatedPrice,
                displayedSubtotal,
                0.01,
                "ERROR: Price mismatch! Accumulated: $" + accumulatedPrice
                        + " | Displayed: $" + displayedSubtotal);

        log.info("Price comparison passed — $" + accumulatedPrice + " == $" + displayedSubtotal);

        // ── Step 9: Click finish & verify success message ─────────────────────
        String successMsg = overviewPage
                .clickOnFinishButton()
                .getSuccessMessageText();

        Assert.assertTrue(
                successMsg.contains("Thank you for your order!"),
                "ERROR: Success message not as expected! Found: " + successMsg);

        log.info("TC04 passed — success message: " + successMsg);
    }
}
