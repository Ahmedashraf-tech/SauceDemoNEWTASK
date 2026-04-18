package Testcases;

import drivers.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import pages.P01_LoginPage;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static drivers.DriverHolder.*;
import static pages.PageBase.quitBrowser;
import static utli.Utility.getSingleJsonData;

@Listeners({listeners.CustomListener.class})
public class TestBase {

    public static double expectedTotalPrice;

    private Properties prop;
    protected String PROJECT_NAME;
    protected String PROJECT_URL;
    protected Logger log;

    // ─── Load properties ─────────────────────────────────────────────────────
    private void setProjectDetails() throws IOException {
        FileInputStream readProperty = new FileInputStream(
                System.getProperty("user.dir")
                        + "/src/test/resources/properties/environment.properties");
        prop = new Properties();
        prop.load(readProperty);
        readProperty.close();
        PROJECT_NAME = prop.getProperty("projectName");
        PROJECT_URL  = prop.getProperty("url");
    }

    public void initialize() throws IOException {
        setProjectDetails();
    }

    // ─── Init logger safely ───────────────────────────────────────────────────
    // Called in BOTH @BeforeSuite and @BeforeMethod
    // so log is never null whether you run a full suite or a single test
    private void initLogger() {
        if (log == null) {
            DOMConfigurator.configure(
                    System.getProperty("user.dir") + "/src/test/resources/log4j.xml");
            log = Logger.getLogger(this.getClass());
        }
    }

    // ─── Suite setup ─────────────────────────────────────────────────────────
    @BeforeSuite
    public void beforeSuite() throws IOException {
        initLogger();
        initialize();
        log.info("=== Suite started | Project: " + PROJECT_NAME + " ===");
    }

    // ─── Open browser before every test ──────────────────────────────────────
    @Parameters("browsername")
    @BeforeMethod
    public void openBrowser(@Optional("chrome") String browsername)
            throws IOException, AWTException {

        initLogger();  // ← FIX: prevents NullPointerException when @BeforeSuite is skipped
        initialize();

        if (PROJECT_URL == null || PROJECT_URL.isEmpty()) {
            throw new RuntimeException("PROJECT_URL is null or empty – check environment.properties");
        }

        WebDriver driver = DriverFactory.getNewInstance(browsername);

        if (driver == null) {
            throw new RuntimeException("DriverFactory returned null for browser: " + browsername);
        }

        setDriver(driver);
        getDriver().manage().window().maximize();
        getDriver().get(PROJECT_URL);
        log.info("Browser opened | Browser: " + browsername + " | URL: " + PROJECT_URL);
    }

    // ─── Shared login helper ──────────────────────────────────────────────────
    protected void loginWithValidCredentials() throws IOException, org.json.simple.parser.ParseException {
        String credPath = System.getProperty("user.dir")
                + "/src/test/resources/testdata/Credential.json";

        String username = getSingleJsonData(credPath, "Username");
        String password = getSingleJsonData(credPath, "Password");

        new P01_LoginPage(getDriver())
                .EnterUserName(username)
                .EnterPassword(password)
                .ClickonLoginButton();

        log.info("Logged in as: " + username);
    }

    // ─── Close browser after every test ──────────────────────────────────────
    @AfterMethod
    public void tearDown() {
        if (getDriver() != null) {
            quitBrowser(getDriver());
            log.info("Browser closed.");
        }
        removeDriver();
    }
}
