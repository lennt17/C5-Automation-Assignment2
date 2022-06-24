package test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import listener.TestNGListener;
import org.testng.annotations.Test;;

import pages.ProjectPage;
import utils.log.LogHelper;
import org.slf4j.Logger;
import org.openqa.selenium.WebDriver;
import pages.HomePage;
import pages.LoginPage;
import pages.TodayPage;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertTrue;

public class TestAsign2B extends TestNGListener {
    Map<String, Object> map = new HashMap<>();
    private String bodyTaskPost = "{\"content\": \"Buy Milk1\", \"due_string\": \"tomorrow at 13:00\", \"due_lang\": \"en\", \"priority\": 4}";

    private static Logger logger = LogHelper.getLogger();
    private HomePage homePage;
    private LoginPage loginPage;
    private TodayPage todayPage;
    private ProjectPage projectPage;
    private drivers.DriverManager DriverManager;
    private String accessToken;

    public TestAsign2B() {
        super();
    }

    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    @Test(description = "successfully")
    public void Test01_CreateProject() throws InterruptedException {
        RestAssured.baseURI = "https://api.todoist.com/rest/v1/";
        basePath = "projects";
        map.put("name", "test 2306");
        Response re = given()
                .header("authorization", "Bearer " + "f15f9ca2b7d9cbe3be967b58681e9b3c0a8d1f0c")
                .header("Content-Type", "application/json")
                .when()
                .body(map)
                .post();
        re.prettyPrint();

        Object res = re.as(Object.class);
        Gson g = new Gson();
        String a = g.toJson(res);
        JsonObject j = g.fromJson(a, JsonObject.class);
        System.out.println("id of new created project: " + j.get("id"));
        System.out.println("Name of new created project: " + j.get("name"));

        String id = (j.get("id")).toString();
        String namePr = (j.get("name")).toString();

        basePath = id;
        final String TASK = "/tasks";
        Response respon = given()
                .header("authorization", "Bearer " + "f15f9ca2b7d9cbe3be967b58681e9b3c0a8d1f0c")
                .header("Content-Type", "application/json")
                .when()
                .body(bodyTaskPost)
                .post(TASK);
        respon.prettyPrint();

        // Web UI
        homePage = new HomePage(action);
        loginPage = homePage.clickLogin();
        todayPage = loginPage.loginAccount("lennt2k@gmail.com", "Len181403032");
        Thread.sleep(3000);
        todayPage.clickSettings();
        accessToken = todayPage.popupSettings.getAccessToken();
        System.out.println(accessToken);

        // to do
        todayPage = todayPage.popupSettings.clickExit();
        projectPage = todayPage.handleMenu.clickProject(namePr);
        projectPage.shouldToBeHaveTask("");
//        projectPage.clickBtnAddNewTask();
//        projectPage.inputNameTask("test task");
//        projectPage.clickBtnAddTask();
//        Thread.sleep(2000);

        //reOpen
        basePath = "tasks";
        final String GET_PROJECT = " /reopen";
        Response respo = given()
                .header("authorization", "Bearer " + "f15f9ca2b7d9cbe3be967b58681e9b3c0a8d1f0c")
                .header("Content-Type", "application/json")
                .post(GET_PROJECT);
        respo.prettyPrint();

        // verify task is reopened (is displayed again)
        projectPage.shouldToBeHaveTask("");
    }
}
