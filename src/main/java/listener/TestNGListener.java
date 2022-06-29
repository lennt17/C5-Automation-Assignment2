package listener;

import WebKeywords.WebKeywords;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestNGListener;
import org.testng.annotations.*;
import pages.HomePage;
import pages.LoginPage;
import pages.TodayPage;
import utils.log.LogHelper;
import org.slf4j.Logger;
import utils.configs.ConfigSettings;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import test.DemoTest;

import io.qameta.allure.Attachment;

import static io.restassured.RestAssured.given;

public class TestNGListener implements ITestNGListener {
    protected WebKeywords action;
    protected String accessToken;

    private static Logger logger = LogHelper.getLogger();
    private ConfigSettings configSettings;

    Gson g = new Gson();

    Map<String, Object> mapLogin = new HashMap<>();

    public TestNGListener() {
        action = new WebKeywords();
        this.accessToken = accessToken ;
        configSettings = new ConfigSettings(System.getProperty("user.dir"));
    }

    @Parameters({"browser"})
    @BeforeTest
    public void beforeTest(String browser) throws InterruptedException {
        deleteFileFromDirectory();
        action.openBrowser(browser, configSettings.getBaseUrl());
        action.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        action.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        RestAssured.baseURI = "https://todoist.com/API/v8.7/user/login";
        mapLogin.put("email", "lennt2k@gmail.com");
        mapLogin.put("password", "Len181403032");
        Response res = given()
                .contentType(ContentType.JSON)
                .and()
                .body(mapLogin)
                .when()
                .post()
                .then()
                .extract().response();
        res.prettyPrint();
        Object response = res.as(Object.class);
        String a = g.toJson(response);
        JsonObject j = g.fromJson(a, JsonObject.class);
        accessToken = (j.get("token")).getAsString();
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
        WebDriver driver = ((DemoTest) testClass).getDriver();

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
