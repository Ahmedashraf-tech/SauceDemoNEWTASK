package retrytests;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int count = 0;                        // per-instance — resets correctly per test
    private static final int MAX_RETRY_COUNT = 1; // 1 retry = 2 runs max

    @Override
    public boolean retry(ITestResult result) {
        if (count < MAX_RETRY_COUNT) {
            count++;
            System.out.println("Retrying test: " + result.getName()
                    + " — attempt " + count + " of " + MAX_RETRY_COUNT);
            return true;
        }
        count = 0; // ← reset عشان لو TestNG reuse نفس الـ instance في test تاني
        return false;
    }
}
