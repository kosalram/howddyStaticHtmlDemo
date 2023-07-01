package utils.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class testNGListener implements ITestListener {

    private static final Logger logger = LoggerFactory.getLogger(testNGListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Executing test: " + result.getName());
        System.out.println("Executing test: " + result.getName());

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test passed: " + result.getName());
        logger.info("Test passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Test failed: " + result.getName());
        logger.error("Test failed: " + result.getName(), result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("Test skipped: " + result.getName());
        logger.warn("Test skipped: " + result.getName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {
        logger.info("Test execution Started.");

    }

    @Override
    public void onFinish(ITestContext context) {

        System.out.println("Test execution finished.");
        logger.info("Test execution finished.");
    }

    // Implement other methods as needed
}
