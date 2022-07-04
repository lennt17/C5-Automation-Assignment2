package test;

import api.ApiProject;
import api.ApiTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import listener.TestNGListener;
import org.testng.annotations.Test;

import pages.ProjectPage;
import org.openqa.selenium.WebDriver;
import pages.HomePage;
import pages.LoginPage;
import pages.TodayPage;
import variable.Token;

import java.util.HashMap;
import java.util.Map;

import static constant.Constant.*;
import static org.testng.Assert.assertTrue;

public class DemoTest extends TestNGListener {
    private String nameProject = "Project 0407";
    private String nameTask = "Task 0407";
    private HomePage homePage;
    private LoginPage loginPage;
    private TodayPage todayPage;
    private ProjectPage projectPage;
    public ApiProject apiProject;
    public ApiTask apiTask;
    private drivers.DriverManager DriverManager;
    Gson g = new Gson();

    public DemoTest() {
        super();
        this.token = new Token();
    }

    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    @Test(description = "Create project and task through API and then verify in WebUI")
    public void Test01_TestAPI(){
        // Init
        String accessToken = token.getToken();
        apiProject = new ApiProject();
        apiTask = new ApiTask();

        // Create project through API
        Map<String, Object> mapPostProject = new HashMap<>();
        mapPostProject.put("name", nameProject);
        JsonObject objectProjectCreated = apiProject.create(accessToken, mapPostProject) ;
        String idProjectCreated = objectProjectCreated.get("id").getAsString();

        // Create task through API
        Map<String, Object> mapPostTask = new HashMap<>();
        mapPostTask.put(project_id, idProjectCreated);
        mapPostTask.put(content, nameTask);
        mapPostTask.put(dueString, "tomorrow at 13:00");
        mapPostTask.put(dueLang, "en");
        mapPostTask.put(priority, 4);
        JsonObject objectTaskCreated = apiTask.create(accessToken, mapPostTask);
        String idTask = objectTaskCreated.get("id").getAsString();

        // Verify value project and task in Web UI
        homePage = new HomePage(action);
        loginPage = homePage.clickLogin();
        todayPage = loginPage.loginAccount("lennt2k@gmail.com", "Len181403032");
        projectPage = todayPage.handleMenu.clickProject(nameProject);
        assertTrue(projectPage.shouldToBeHaveTask(nameTask));

        // Click checkbox task in order to not display task in WebUI and then verify
        projectPage.clickCheckboxTask(nameTask);
        assertTrue(projectPage.shouldToBeNotDisplayTask(nameTask));

        // ReOpen task through API and then verify task is reopened in WebUI (is displayed again)
        apiTask.reOpenTask(accessToken, idTask);
        action.refresh();
        assertTrue(projectPage.shouldToBeHaveTask(nameTask));
    }
}
