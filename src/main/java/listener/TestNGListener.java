package listener;

import WebKeywords.WebKeywords;

import utils.log.LogHelper;
import org.slf4j.Logger;
import utils.configs.ConfigSettings;
import java.io.File;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestNGListener;
import org.testng.ITestResult;
import test.TestAsign2B;

import io.qameta.allure.Attachment;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

public class TestNGListener {
    protected WebKeywords action;

    private static Logger logger = LogHelper.getLogger();
    private ConfigSettings configSettings;

    public TestNGListener() {
        action = new WebKeywords();
        configSettings = new ConfigSettings(System.getProperty("user.dir"));
    }

    @Parameters({ "browser" })
    @BeforeTest
    public void beforeTest(String browser) {
        deleteFileFromDirectory();
        action.openBrowser(browser, configSettings.getBaseUrl());
    }

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    // Text attachments for allure
    @Attachment(value = "Form screenshot", type = "image/png")
    public byte[] saveScreenshotPNG(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    // Text attachments for allure
    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    // @Override
    public void onTestFailure(ITestResult iTestResult) {
        System.out.print("I am in onTestFailure method " + getTestMethodName(iTestResult) + "failed");

        // Get driver from DemoTest and assign to local webdriver variable
        Object testClass = iTestResult.getInstance();
        WebDriver driver = ((TestAsign2B) testClass).getDriver();

        // Allure ScreenshotRobot and SaveTestLog
        if (driver instanceof WebDriver) {
            System.out.println("Screenshot captured for test case: " + getTestMethodName(iTestResult));
            saveScreenshotPNG(driver);
        }

        // Save a log on allure
        saveTextLog(getTestMethodName(iTestResult) + " failed and screenshot taken!");
    }

    public void deleteFileFromDirectory() {
        //String user = System.getProperty("user home");   // user if data in your user profile
        //String filePath = user + "/Downloads/Test;
        String directory = "D:\\C5-VMO-git\\C5-Automation-Assignment2B\\target\\allure-results"; // If download is in IDE project folder

        File file = new File(directory);
        String[] currentFiles;
        if (file.isDirectory()) {
            currentFiles = file.list();
            for (int i = 0; i < currentFiles.length; i++) {
                File myFile = new File(file, currentFiles[i]);
                myFile.delete();
            }
        }
    }

    @AfterTest
   public void afterTest() {
        action.closeBrowser(configSettings.getBaseUrl());
    }
}
