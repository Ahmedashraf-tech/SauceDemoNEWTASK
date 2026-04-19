package listeners;

import common.MyScreenRecorder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static drivers.DriverHolder.getDriver;

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
        takeScreenshot(result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
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

    // ── Take Screenshot ────────────────────────────────────────────────────────
    public void takeScreenshot(String testName) {
        try {
            // ← FIX: ينشئ الـ Screenshots folder تلقائياً لو مش موجود
            File screenshotDir = new File(
                    System.getProperty("user.dir") + "/src/test/resources/Screenshots/");
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            // ← FIX: تأكد إن الـ driver مش null قبل الـ screenshot
            if (getDriver() == null) {
                System.err.println("[CustomListener] Driver is null — skipping screenshot for: " + testName);
                return;
            }

            String screenshotName = testName + new Date().toString()
                    .replace(" ", "-")
                    .replace(":", "-");

            TakesScreenshot ts = (TakesScreenshot) getDriver();
            FileHandler.copy(
                    ts.getScreenshotAs(OutputType.FILE),
                    new File(screenshotDir + "/" + screenshotName + ".png")
            );

            System.out.println("[CustomListener] Screenshot saved: " + screenshotName + ".png");

        } catch (WebDriverException | IOException e) {
            System.err.println("[CustomListener] Screenshot failed: " + e.getMessage());
        }
    }
}
