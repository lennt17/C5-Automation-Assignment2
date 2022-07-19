package test;

import accessToken.Token;
import api.ApiProject;
import com.google.gson.JsonObject;
import handle.HandleResponse;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static constant.Constant.*;
import static microservices.Projects.models.Project.*;
import static org.testng.Assert.assertEquals;
import static test.TC_CreateProject.idProjectCreated;

public class TC_GetAProject {
    ApiProject apiProject = new ApiProject();
    HandleResponse handleResponse = new HandleResponse();
    Token token = new Token();

    @Test(description = "API: Get a project - successfully")
    public void Test01_getAProject() {
        String accessToken = token.getToken();

        long idProjectGet = idProjectCreated;

        Response res = apiProject.getAProject(accessToken, idProjectGet);
        JsonObject ObjectProjectGot = handleResponse.getJsonObject(res);
        System.out.println(ObjectProjectGot);
        long idProjectGot = ObjectProjectGot.get("id").getAsLong();
        int statusCode = handleResponse.getStatusCode(res);

        assertEquals(idProjectGet, idProjectGot);
        assertEquals(statusCode, 200);
    }

    @Test(description = "API: Get a project - without token")
    public void Test02_getAProjectWithoutToken() {
        String accessToken = "";
        long idProjectGet = idProjectCreated;

        Response res = apiProject.getAProject(accessToken, idProjectGet);
        int statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 403);
    }

    @Test(description = "API: Get a project - with non-existing token")
    public void Test03_getAProjectWithNonExistingToken() {
        String accessToken = "!@#123";
        long idProjectGet = idProjectCreated;

        Response res = apiProject.getAProject(accessToken, idProjectGet);
        int statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 401);
    }

    @Test(description = "API: Get a project - with expired token")
    public void Test04_getAProjectWithExpiredToken() {
        String accessToken = tokenExpired;
        long idProjectGet = idProjectCreated;

        Response res = apiProject.getAProject(accessToken, idProjectGet);
        int statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 401);
    }

    @Test(description = "API: Get a project - with different method")
    public void Test05_getAProjectWithDifferentMethod() {
        String accessToken = token.getToken();
        long idProjectGet = idProjectCreated;

        Map<String, Object> mapPost = new HashMap<>();
        mapPost.put(name, "diff method");

        Response res = apiProject.getAProjectWithPostMethod(accessToken, idProjectGet, mapPost);
        int statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 400);
    }

    @Test(description = "API: Get a project - with invalid value of id project")
    public void Test06_getAProjectWithInvalidValueOfIdProject() {
        String accessToken = token.getToken();
        long idProjectGet = idProjectInvalid;

        Response res = apiProject.getAProject(accessToken, idProjectGet);
        int statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 404);
    }
}
