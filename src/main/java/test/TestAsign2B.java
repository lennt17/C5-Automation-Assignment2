package test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class TestAsign2B extends TestNGListener {
    Map<String, Object> map = new HashMap<>();
    private String namePr = "2406";
    private String nameTask = "Buy Milk new";
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

    @Test(description = "Create project and task through API and then verify in WebUI")
    public void Test01_CreateProject() throws InterruptedException {

        // create project through API
        RestAssured.baseURI = "https://api.todoist.com/rest/v1/";
        basePath = "projects";
        map.put("name", namePr);
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

        // create task through API
        basePath = "/tasks";
//        String bodyTaskPost = "{\"project_id\": "+ j.get("id") + ", \"content\": " + nameTask + ", \"due_string\": \"tomorrow at 13:00\", \"due_lang\": \"en\", \"priority\": 4}";

        String bodyTaskPost = "{\"project_id\": "+ j.get("id") + ", \"content\": \"Buy Milk new\", \"due_string\": \"tomorrow at 13:00\", \"due_lang\": \"en\", \"priority\": 4}";

        Response responTask = given()
                .header("authorization", "Bearer " + "f15f9ca2b7d9cbe3be967b58681e9b3c0a8d1f0c")
                .header("Content-Type", "application/json")
                .when()
                .body(bodyTaskPost)
                .post();
        responTask.prettyPrint();

        Object responT = responTask.as(Object.class);
        String resTask = g.toJson(responT);
        JsonObject k = g.fromJson(resTask, JsonObject.class);
        String str_id_task = (k.get("id")).toString();

        // Verify project and task in Web UI
        homePage = new HomePage(action);
        loginPage = homePage.clickLogin();
        todayPage = loginPage.loginAccount("lennt2k@gmail.com", "Len181403032");
        Thread.sleep(3000);
        todayPage.clickSettings();
        accessToken = todayPage.popupSettings.getAccessToken();

        // verify task in WebUI
        todayPage = todayPage.popupSettings.clickExit();
        projectPage = todayPage.handleMenu.clickProject(namePr);
        assertTrue(projectPage.shouldToBeHaveTask(nameTask));

        // click checkbox task to not display task and verify
        projectPage.clickCheckboxTask();
        Thread.sleep(3000);
        assertFalse(projectPage.shouldToBeNotDisplayTask());

        //reOpen task through API
        basePath = "/tasks/" + str_id_task;
        final String REOPEN =  "/reopen";
        Response resp = given()
                .header("authorization", "Bearer " + "f15f9ca2b7d9cbe3be967b58681e9b3c0a8d1f0c")
                .header("Content-Type", "application/json")
                .post(REOPEN);
        resp.prettyPrint();

        Thread.sleep(3000);

        // verify task is reopened in WebUI (is displayed again)
        assertTrue(projectPage.shouldToBeHaveTask(nameTask));
    }
}
