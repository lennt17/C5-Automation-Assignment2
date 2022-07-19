package test;

import api.ApiProject;
import api.ApiTask;
import com.google.gson.JsonObject;
import handle.HandleResponse;
import handle.Handles;
import io.restassured.response.Response;
import listener.TestNGListener;
import microservices.Projects.steps.Project;
import microservices.Tasks.steps.Tasks;
import org.testng.annotations.Test;

import pages.ProjectPage;
import pages.HomePage;
import pages.LoginPage;
import pages.TodayPage;
import utils.configs.ConfigSettings;
import accessToken.Token;

import java.util.HashMap;
import java.util.Map;

import static microservices.Tasks.models.Task.*;
import static microservices.Projects.models.Project.*;
import static org.testng.Assert.assertTrue;

public class TC_DemoTest2B extends TestNGListener {
    public final String nameProject = "Project 0607";
    public final String nameTask = "Task 0607";
    public HomePage homePage;
    public LoginPage loginPage;
    public TodayPage todayPage;
    public ProjectPage projectPage;
    public ConfigSettings configSettings;
    HandleResponse handleResponse = new HandleResponse();
    ApiProject apiProject = new ApiProject();
    Project project = new Project();
    Handles handles = new Handles();
    ApiTask apiTask = new ApiTask();
    Tasks tasks = new Tasks();
    Token token = new Token();

    public TC_DemoTest2B() {
        super();
        configSettings = new ConfigSettings(System.getProperty("user.dir"));
    }

    @Test(description = "API & UI: Create project and task through API and then verify in WebUI")
    public void Test2B_createProjectAndTaskThenVerifyInUI() {
        String accessToken = token.getToken();
        handles.deleteAllProjects();

        // Create project through API
        Map<String, Object> mapPostProject = new HashMap<>();
        mapPostProject.put(name, nameProject);

        Response res = apiProject.createProject(accessToken, mapPostProject);

        JsonObject objectProjectCreated = handleResponse.getJsonObject(res);
        long idProjectCreate = project.getIdProject(objectProjectCreated);

        // Create task through API
        Map<String, Object> mapPostTask = new HashMap<>();
        mapPostTask.put(project_id, idProjectCreate);
        mapPostTask.put(content, nameTask);
        mapPostTask.put(dueString, "tomorrow at 13:00");
        mapPostTask.put(dueLang, "en");
        mapPostTask.put(priority, 4);

        Response response = apiTask.createTask(accessToken, mapPostTask);
        JsonObject objectTaskCreated = handleResponse.getJsonObject(response);
        long idTask = tasks.getIdTask(objectTaskCreated); ;

        // Verify value project and task in Web UI
        homePage = new HomePage(action);
        loginPage = homePage.clickLogin();
        String email = configSettings.getEmail();
        String password = configSettings.getPassword();
        todayPage = loginPage.loginAccount(email, password);
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
