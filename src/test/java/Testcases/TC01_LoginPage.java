package Testcases;

import io.qameta.allure.*;
import retrytests.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.json.simple.parser.ParseException;
import pages.P01_LoginPage;

import java.io.IOException;

import static drivers.DriverHolder.getDriver;
import static utli.Utility.getSingleJsonData;

@Epic("Login")
@Story("Login Story")
public class TC01_LoginPage extends TestBase {

    private static final String CRED_PATH =
            System.getProperty("user.dir")
                    + "\\src\\test\\resources\\testdata\\Credential.json";

    // ─── Positive: valid credentials ─────────────────────────────────────────
    @Description("Login with Valid Credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 2,
            description = "Login to SauceDemo with valid username and password",
            retryAnalyzer = RetryAnalyzer.class)
    public void loginToSauceDemo_P() throws IOException, ParseException {

        String username = getSingleJsonData(CRED_PATH, "Username");
        String password = getSingleJsonData(CRED_PATH, "Password");

        new P01_LoginPage(getDriver())
                .EnterUserName(username)
                .EnterPassword(password)
                .ClickonLoginButton();

        log.info("Login Successfully — user: " + username);

        Assert.assertEquals(
                getDriver().getCurrentUrl(),
                "https://www.saucedemo.com/inventory.html",
                "ERROR: URL after login does not match expected inventory page.");
    }

    // ─── Negative: invalid credentials ───────────────────────────────────────
    @Description("Login with Invalid Credentials")
    @Severity(SeverityLevel.NORMAL)
    @Test(priority = 1,
            description = "Login to SauceDemo with invalid username and password",
            retryAnalyzer = RetryAnalyzer.class,
            enabled = true)
    public void loginToSauceDemo_N() throws IOException, ParseException {

        String invalidUsername = getSingleJsonData(CRED_PATH, "InvalidUsername");
        String invalidPassword = getSingleJsonData(CRED_PATH, "InvalidPassword");

        // ✅ instance واحد بس — نفس الـ object بيعمل login وبيجيب الـ error
        P01_LoginPage loginPage = new P01_LoginPage(getDriver());
        loginPage
                .EnterUserName(invalidUsername)
                .EnterPassword(invalidPassword)
                .ClickonLoginButton();

        log.info("Login Failed — user: " + invalidUsername);

        String expectedMessage =
                "Epic sadface: Username and password do not match any user in this service";
        String actualMessage = loginPage.getErrorMessage();

        Assert.assertEquals(actualMessage, expectedMessage,
                "ERROR: Error message does not match expected text.");
    }
}
