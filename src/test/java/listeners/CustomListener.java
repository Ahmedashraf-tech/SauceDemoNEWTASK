package listeners;

import common.MyScreenRecorder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import static drivers.DriverHolder.getDriver;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class CustomListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        try {
            ITestListener.super.onTestStart(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onTestSuccess(ITestResult result) {
    // ITestListener.super.onTestSuccess(result);
    takeScreenshot(result.getName());

    }

    @Override
    public void onTestFailure(ITestResult result) {
        // take screenshot on test failure
        takeScreenshot(result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ITestListener.super.onTestSkipped(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
    }

    @Override
    public void onStart(ITestContext context) {
        try {
            MyScreenRecorder.startRecording(context.getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        try {
            MyScreenRecorder.stopRecording();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: Capture Screenshot

    public void takeScreenshot(String testName) {
        // WebDriver driver = new ChromeDriver();
        TakesScreenshot takesScreenshot = (TakesScreenshot) getDriver();
        Date currntDate = new Date();
        String screenshotName = testName+currntDate.toString().replace(" ", "-").replace(":", "-");
        try {
            FileHandler.copy(takesScreenshot.getScreenshotAs(OutputType.FILE), new File(System.getProperty("user.dir")
                    + "/src/test/resources/Screenshots/" + screenshotName + ".png"));
        } catch (WebDriverException | IOException e) {
            e.printStackTrace();
        }
    }
}

