package test;

import accessToken.Token;
import api.APIBase;
import api.ApiProject;
import com.google.gson.JsonArray;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static constant.Constant.*;

public class TC_GetAllProject {
    ApiProject apiProject = new ApiProject();
    APIBase apiBase = new APIBase();
    Token token = new Token();

    @Test(description = "API: Get all projects - with valid token successfully")
    public void Test01_getAllProject() {
        String accessToken = token.getToken();

        Response res = apiProject.getAllProjects(accessToken);
        int statusCode = apiBase.getStatusCode(res);
        System.out.println(statusCode);
        JsonArray arr = apiBase.getJsonArray(res);
        System.out.println(arr);

        assertEquals(statusCode, 200);
    }

    @Test(description = "API: Get all projects - when non-existing project in account")
    public void Test02_getAllProjectWhenNonExistingProject(){
        String accessToken = token.getToken();

        //precondition: non-existing project in account
        Response res = apiProject.getAllProjects(accessToken);
        int statusCode = apiBase.getStatusCode(res);
        assertEquals(statusCode, 200);
    }

    @Test(description = "API: Get all projects - without token")
    public void Test03_getAllProjectWithoutToken() {
        String accessToken = "";

        Response res = apiProject.getAllProjects(accessToken);
        int statusCode = apiBase.getStatusCode(res);
        assertEquals(statusCode, 401);
    }

    @Test(description = "API: Get all projects - with non-existing token")
    public void Test04_getAllProjectWithNonExistingToken() {
        String accessToken = "!@#123";

        Response res = apiProject.getAllProjects(accessToken);
        int statusCode = apiBase.getStatusCode(res);
        assertEquals(statusCode, 401);
    }

    @Test(description = "API: Get all projects - with expired token")
    public void Test05_getAllProjectWithExpiredToken(){
        String accessToken = tokenExpired;

        Response res = apiProject.getAllProjects(accessToken);
        int statusCode = apiBase.getStatusCode(res);
        assertEquals(statusCode, 401);
    }
}
