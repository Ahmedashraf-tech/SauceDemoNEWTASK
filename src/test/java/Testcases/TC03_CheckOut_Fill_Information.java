package Testcases;

import io.qameta.allure.*;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.P02_ProductsPage;
import pages.P03_Checkout_Fill_Information;
import retrytests.RetryAnalyzer;

import java.io.IOException;

import static drivers.DriverHolder.getDriver;

@Epic("Checkout")
@Story("Checkout Fill Information Story")
public class TC03_CheckOut_Fill_Information extends TestBase {

    private static final String CHECKOUT_STEP_ONE_URL = "https://www.saucedemo.com/checkout-step-one.html";
    private static final String CHECKOUT_STEP_TWO_URL = "https://www.saucedemo.com/checkout-step-two.html";

    @Feature("Checkout Step One")
    @Description("Verify the ability to fill random customer information and proceed to order overview")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 1,
            description = "Verify the ability to pass random data to information page",
            retryAnalyzer = RetryAnalyzer.class)
    public void fillInformation() throws IOException, ParseException {

        // ── Step 1: Login ──────────────────────────────────────────────────────
        loginWithValidCredentials();

        // ── Step 2: Add products → go to cart → go to checkout step one ───────
        P02_ProductsPage productsPage = new P02_ProductsPage(getDriver());
        productsPage.addRandomProductsToCart();
        productsPage.clickOnCartIcon();

        getDriver().get(CHECKOUT_STEP_ONE_URL);

        Assert.assertEquals(
                getDriver().getCurrentUrl(),
                CHECKOUT_STEP_ONE_URL,
                "ERROR: Did not reach checkout step one page!");

        // ── Step 3: Fill information & continue ───────────────────────────────
        new P03_Checkout_Fill_Information(getDriver())
                .EnterFirstName()
                .EnterLastName()
                .EnterZIPCode()
                .ClickOnContinue();

        // ── Step 4: Assert we moved to checkout step two ──────────────────────
        Assert.assertEquals(
                getDriver().getCurrentUrl(),
                CHECKOUT_STEP_TWO_URL,
                "ERROR: Did not navigate to the checkout overview page!");

        log.info("TC03 passed — navigated to: " + getDriver().getCurrentUrl());
    }
}
