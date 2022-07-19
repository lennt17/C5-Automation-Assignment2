package test;

import accessToken.Token;
import api.ApiProject;
import com.google.gson.JsonObject;
import handle.HandleResponse;
import io.restassured.response.Response;
import microservices.Projects.steps.Project;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static constant.Constant.*;
import static test.TC_CreateProject.idProjectCreated;
import static microservices.Projects.models.Project.*;

public class TC_UpdateProject {
    ApiProject apiProject = new ApiProject();
    Project project = new Project();
    HandleResponse handleResponse = new HandleResponse();
    Token token = new Token();

    // Test api update project
    @Test(description = "API: Update project - successfully with valid token and valid optional field")
    public void Test01_updateProject() {
        String accessToken = token.getToken();

        long idProjectUpdate = idProjectCreated;
        Map<String, Object> mapPut = new HashMap<>();
        String nameProjectUpdate = "Things To";
        mapPut.put(name, nameProjectUpdate);

        Response res = apiProject.updateProject(accessToken, idProjectUpdate, mapPut);
        int statusCode = handleResponse.getStatusCode(res);
        JsonObject ObjectProjectUpdated = handleResponse.getJsonObject(apiProject.getAProject(accessToken, idProjectUpdate));

        String nameProjectUpdated = project.getNameProject(ObjectProjectUpdated);

        assertEquals(statusCode, 204);
        assertEquals(nameProjectUpdated, nameProjectUpdate);
    }

    @Test(description = "API: Update project - send request with same body")
    public void Test02_updateProjectWithSameBody() {
        String accessToken = token.getToken();
        long idProjectGet = idProjectCreated;

        // send request1
        Map<String, Object> mapPut = new HashMap<>();
        mapPut.put(name, "updateTwice");

        Response res = apiProject.updateProject(accessToken, idProjectGet, mapPut);
        int statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 204);

        // send request2
        Map<String, Object> mapPut2 = new HashMap<>();
        mapPut2.put(name, "updateTwice");

        Response res2 = apiProject.updateProject(accessToken, idProjectGet, mapPut2);
        int statusCode2 = handleResponse.getStatusCode(res2);
        assertEquals(statusCode2, 400);
    }

    @Test(description = "API: Update project - with field undefine")
    public void Test03_updateProjectWithFieldUndefine() {
        String accessToken = token.getToken();

        long idProjectUpdate = idProjectCreated;
        Map<String, Object> mapPut = new HashMap<>();
        String nameProjectUpdate = "Things To Buy";
        mapPut.put(name, nameProjectUpdate);
        mapPut.put("constant", 34);

        Response res = apiProject.updateProject(accessToken, idProjectUpdate, mapPut);
        int statusCode = handleResponse.getStatusCode(res);

        assertEquals(statusCode, 400);
    }

    @Test(description = "API: Update project - with invalid type of optional field")
    public void Test04_updateProjectWithInvalidTypeOfOptionField() {
        String accessToken = token.getToken();

        long idProjectUpdate = idProjectCreated;

        // map for update name
        Map<String, Object> mapPutName = new HashMap<>();
        mapPutName.put(name, 123);

        Response resName = apiProject.updateProject(accessToken, idProjectUpdate, mapPutName);
        int statusCode1 = handleResponse.getStatusCode(resName);

        assertEquals(statusCode1, 400);

        // map for update color
        Map<String, Object> mapPutColor = new HashMap<>();
        mapPutColor.put(color, "green");

        Response resColor = apiProject.updateProject(accessToken, idProjectUpdate, mapPutColor);
        int statusCode2 = handleResponse.getStatusCode(resColor);

        assertEquals(statusCode2, 400);

        // map for update favorite
        Map<String, Object> mapPutFavorite = new HashMap<>();
        mapPutFavorite.put(favorite, 123);

        Response resFavorite = apiProject.updateProject(accessToken, idProjectUpdate, mapPutFavorite);
        int statusCode3 = handleResponse.getStatusCode(resFavorite);

        assertEquals(statusCode3, 400);
    }

    @Test(description = "API: Update project - with invalid value of optional field")
    public void Test05_updateProjectWithInvalidValueOfOptionalField() {
        String accessToken = token.getToken();
        long idProjectUpdate = idProjectCreated;

        // name = ""
        Map<String, Object> mapPut1 = new HashMap<>();
        mapPut1.put(name, "");
        Response res1 = apiProject.updateProject(accessToken, idProjectUpdate, mapPut1);
        int statusCode1 = handleResponse.getStatusCode(res1);
        assertEquals(statusCode1, 400);

        // favorite = null
        Map<String, Object> mapPut2 = new HashMap<>();
        mapPut2.put(favorite, null);
        Response res2 = apiProject.updateProject(accessToken, idProjectUpdate, mapPut2);
        int statusCode2 = handleResponse.getStatusCode(res2);
        assertEquals(statusCode2, 400);

        // color = 56789
        Map<String, Object> mapPut3 = new HashMap<>();
        mapPut3.put(color, 56789);
        Response res3 = apiProject.updateProject(accessToken, idProjectUpdate, mapPut3);
        int statusCode3 = handleResponse.getStatusCode(res3);
        assertEquals(statusCode3, 400);
    }

    @Test(description = "API: Update project - without token")
    public void Test06_updateProjectWithoutToken() {
        String accessToken = "";
        long idProjectUpdate = idProjectCreated;

        Map<String, Object> mapPut = new HashMap<>();
        mapPut.put(name, "change");

        Response res = apiProject.updateProject(accessToken, idProjectUpdate, mapPut);
        int statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 401);
    }

    @Test(description = "API: Update project - with non-existing token")
    public void Test07_updateProjectWithNonExistingToken() {
        String accessToken = "!@#123";
        long idProjectUpdate = idProjectCreated;

        Map<String, Object> mapPut = new HashMap<>();
        mapPut.put(name, "change");

        Response res = apiProject.updateProject(accessToken, idProjectUpdate, mapPut);
        int statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 401);
    }

    @Test(description = "API: Update project - with expired token")
    public void Test08_updateProjectWithExpiredToken() {
        String accessToken = tokenExpired;
        long idProjectUpdate = idProjectCreated;

        Map<String, Object> mapPut = new HashMap<>();
        mapPut.put(name, "change");

        Response res = apiProject.updateProject(accessToken, idProjectUpdate, mapPut);
        int statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 401);
    }

    @Test(description = "Update project with different method")
    public void Test9_updateProjectWithDifferentMethod() {
        String accessToken = token.getToken();
        long idProjectUpdate = idProjectCreated;

        Map<String, Object> mapPut = new HashMap<>();

        Response res = apiProject.updateProjectWithGetMethod(accessToken, idProjectUpdate, mapPut);
        int statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 400);
    }

    @Test(description = "API: Update project - with empty body")
    public void Test10_updateProjectWithEmptyBody() {
        String accessToken = token.getToken();
        long idProjectUpdate = idProjectCreated;

        Map<String, Object> mapPut = new HashMap<>();

        Response res = apiProject.updateProject(accessToken, idProjectUpdate, mapPut);
        int statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 400);
    }
}
