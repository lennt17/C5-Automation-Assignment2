package test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import listener.TestNGListener;
import org.testng.annotations.Test;

import pages.ProjectPage;
import org.openqa.selenium.WebDriver;
import pages.HomePage;
import pages.LoginPage;
import pages.TodayPage;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertTrue;

public class DemoTest extends TestNGListener {
    Map<String, Object> mapProject = new HashMap<>();
    Map<String, Object> mapTask = new HashMap<>();
    private String nameProject = "Project 2806";
    private String nameTask = "Task 2806";
    private HomePage homePage;
    private LoginPage loginPage;
    private TodayPage todayPage;
    private ProjectPage projectPage;
    private drivers.DriverManager DriverManager;
    Gson g = new Gson();

    public DemoTest() {
        super();
    }

    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    @Test(description = "Create project and task through API and then verify in WebUI")
    public void Test01_TestAPI() throws InterruptedException {

        // create project through API
        RestAssured.baseURI = "https://api.todoist.com/rest/v1/";
        basePath = "projects";
        mapProject.put("name", nameProject);
        Response re = given()
                .header("authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .when()
                .body(mapProject)
                .post();
        re.prettyPrint();

        Object res = re.as(Object.class);
        String a = g.toJson(res);
        JsonObject j = g.fromJson(a, JsonObject.class);

        // create task through API
        basePath = "/tasks";
        mapTask.put("project_id", j.get("id").getAsString());
        mapTask.put("content", nameTask);
        mapTask.put("due_string", "tomorrow at 13:00");
        mapTask.put("due_lang", "en");
        mapTask.put("priority", 4);

        Response responTask = given()
                .header("authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .when()
                .body(mapTask)
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
        projectPage = todayPage.handleMenu.clickProject(nameProject);
        Thread.sleep(2000);
        assertTrue(projectPage.shouldToBeHaveTask(nameTask));

        // click checkbox task to not display task and verify
        projectPage.clickCheckboxTask(nameTask);
        Thread.sleep(3000);
        assertTrue(projectPage.shouldToBeNotDisplayTask(nameTask));

        //reOpen task through API
        basePath = "/tasks/" + str_id_task;
        final String REOPEN = "/reopen";
        Response resp = given()
                .header("authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .post(REOPEN);
        resp.prettyPrint();

        action.refresh();
        Thread.sleep(3000);

        // verify task is reopened in WebUI (is displayed again)
        assertTrue(projectPage.shouldToBeHaveTask(nameTask));
    }
}
