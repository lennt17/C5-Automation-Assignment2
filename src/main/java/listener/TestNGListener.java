package listener;

import WebKeywords.WebKeywords;

import org.testng.ITestNGListener;
import org.testng.annotations.*;
import utils.configs.ConfigSettings;

import java.io.File;
import java.time.Duration;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

import io.qameta.allure.Attachment;

public class TestNGListener implements ITestNGListener {
    protected WebKeywords action;
    private ConfigSettings configSettings;
    private drivers.DriverManager driverManager;

    public TestNGListener() {
        action = new WebKeywords();
        configSettings = new ConfigSettings(System.getProperty("user.dir"));
    }

    public WebDriver getDriver() {
        return driverManager.getDriver();
    }

    @Parameters({"browser"})
    @BeforeTest
    public void beforeTest(String browser){
        deleteFileFromDirectory();
        action.openBrowser(browser, configSettings.getBaseUrl());
        action.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        action.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(45));
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

        WebDriver driver = getDriver();

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
