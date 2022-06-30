package test;

import api.Api;
import com.google.gson.Gson;
import listener.TestNGListener;
import org.testng.annotations.Test;

import pages.ProjectPage;
import org.openqa.selenium.WebDriver;
import pages.HomePage;
import pages.LoginPage;
import pages.TodayPage;
import static org.testng.Assert.assertTrue;

public class DemoTest extends TestNGListener {
    private String nameProject = "Project 2806";
    private String nameTask = "Task 2806";
    private HomePage homePage;
    private LoginPage loginPage;
    private TodayPage todayPage;
    private ProjectPage projectPage;
    public Api api;
    private drivers.DriverManager DriverManager;
    Gson g = new Gson();
    String idProjectCreated;
    String idTask;

    public DemoTest() {
        super();
    }

    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    @Test(description = "Create project and task through API and then verify in WebUI")
    public void Test01_TestAPI(){
        api = new Api();
        idProjectCreated = api.createProject(nameProject) ;
        idTask = api.createTask(nameTask, idProjectCreated);

        //// Verify project and task in Web UI
        homePage = new HomePage(action);
        loginPage = homePage.clickLogin();
        todayPage = loginPage.loginAccount("lennt2k@gmail.com", "Len181403032");
        projectPage = todayPage.handleMenu.clickProject(nameProject);
        assertTrue(projectPage.shouldToBeHaveTask(nameTask));

        // click checkbox task to not display task and verify
        projectPage.clickCheckboxTask(nameTask);
        assertTrue(projectPage.shouldToBeNotDisplayTask(nameTask));

        //reOpen task through API
        api.reOpenTask(idTask);
        action.refresh();

        // verify task is reopened in WebUI (is displayed again)
        assertTrue(projectPage.shouldToBeHaveTask(nameTask));
    }
}
