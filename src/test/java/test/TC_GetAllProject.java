package test;

import accessToken.Token;
import api.ApiProject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import handle.HandleResponse;
import handle.Handles;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static constant.Constant.*;

public class TC_GetAllProject {
    ApiProject apiProject = new ApiProject();
    HandleResponse handleResponse = new HandleResponse();
    Handles handles = new Handles();
    Token token = new Token();

    @Test(description = "API: Get all projects successfully")
    @Description("200 - Get all projects successfully with valid token")
    public void Test01_getAllProject() {
        String accessToken = token.getToken();

        Response res = apiProject.getAllProjects(accessToken);
        int statusCode = handleResponse.getStatusCode(res);
        System.out.println(statusCode);
        JsonArray arr = handleResponse.getJsonArray(res);
        System.out.println(arr);

        assertEquals(statusCode, 200);
    }

    @Test(description = "API: Get all projects when non-existing project in account")
    @Description("200 - Get all projects when non-existing project in account")
    public void Test02_getAllProjectWhenNonExistingProject() {
        String accessToken = token.getToken();
        handles.deleteAllProjects();

        Response res = apiProject.getAllProjects(accessToken);
        int statusCode = handleResponse.getStatusCode(res);
        JsonArray arr = handleResponse.getJsonArray(res);
        int numberOfProject = arr.size();

        assertEquals(statusCode, 200);
        assertEquals(numberOfProject, 0);
    }

    @Test(description = "API: Get all projects without token")
    @Description("401 - Get all projects without token")
    public void Test03_getAllProjectWithoutToken() {
        String accessToken = "";

        Response res = apiProject.getAllProjects(accessToken);
        int statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 401);
    }

    @Test(description = "API: Get all projects with non-existing token")
    @Description("401 - Get all projects with non-existing token")
    public void Test04_getAllProjectWithNonExistingToken() {
        String accessToken = "!@#123";

        Response res = apiProject.getAllProjects(accessToken);
        int statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 401);
    }

    @Test(description = "API: Get all projects with expired token")
    @Description("401 - Get all projects with expired token")
    public void Test05_getAllProjectWithExpiredToken() {
        String accessToken = tokenExpired;

        Response res = apiProject.getAllProjects(accessToken);
        int statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 401);
    }

    @Test(description = "API: Get all projects with different method")
    @Description("400 - Get all projects with different method")
    public void Test06_getAllProjectWithDifferentMethod() {
        String accessToken = token.getToken();

        Map<String, Object> mapPost = new HashMap<>();

        Response res = apiProject.getAllProjectsWithPostMethod(accessToken, mapPost);
        int statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 400);
    }
}
