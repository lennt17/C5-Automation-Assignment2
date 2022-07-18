package test;

import accessToken.Token;
import api.APIBase;
import api.ApiProject;
import com.google.gson.JsonObject;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static constant.Constant.*;

public class TC_UpdateProject {
    public TC_CreateProject createProject;
    ApiProject apiProject = new ApiProject();
    APIBase apiBase = new APIBase();
    Token token = new Token();

    // Test api update project
    @Test(description = "API: Update project - successfully with valid token and valid optional field")
    public void Test01_updateProject() {
        String accessToken = token.getToken();

        long idProjectUpdate = createProject.idProjectCreated;
        Map<String, Object> mapPut = new HashMap<>();
        String nameProjectUpdate = "Things To";
        mapPut.put("name", nameProjectUpdate);

        Response res = apiProject.updateProject(accessToken, idProjectUpdate, mapPut);
        int statusCode = apiBase.getStatusCode(res);
        JsonObject ObjectProjectUpdated = apiBase.getJsonObject(apiProject.getAProject(accessToken, idProjectUpdate));
        String nameProjectUpdated = ObjectProjectUpdated.get("name").getAsString();
        System.out.println(ObjectProjectUpdated);

        assertEquals(statusCode, 204);
        assertEquals(nameProjectUpdated, nameProjectUpdate);
    }

    @Test(description = "API: Update project - with field undefine")
    public void Test04_updateProjectWithFieldUndefine() {
        String accessToken = token.getToken();

        long idProjectUpdate = createProject.idProjectCreated;
        Map<String, Object> mapPut = new HashMap<>();
        String nameProjectUpdate = "Things To Buy";
        mapPut.put("name", nameProjectUpdate);
        mapPut.put("constant", 34);

        Response res = apiProject.updateProject(accessToken, idProjectUpdate, mapPut);
        int statusCode = apiBase.getStatusCode(res);

        assertEquals(statusCode, 400);
    }

    @Test(description = "API: Update project - with invalid type of optional field")
    public void Test05_updateProjectWithInvalidTypeOfOptionField() {
        String accessToken = token.getToken();

        long idProjectUpdate = createProject.idProjectCreated;

        // map for update name
        Map<String, Object> mapPutName = new HashMap<>();
        mapPutName.put("name", 123);

        Response resName = apiProject.updateProject(accessToken, idProjectUpdate, mapPutName);
        int statusCode1 = apiBase.getStatusCode(resName);

        assertEquals(statusCode1, 400);

        // map for update color
        Map<String, Object> mapPutColor = new HashMap<>();
        mapPutColor.put("color", "green");

        Response resColor = apiProject.updateProject(accessToken, idProjectUpdate, mapPutColor);
        int statusCode2 = apiBase.getStatusCode(resColor);

        assertEquals(statusCode2, 400);

        // map for update favorite
        Map<String, Object> mapPutFavorite = new HashMap<>();
        mapPutFavorite.put("favorite", 123);

        Response resFavorite = apiProject.updateProject(accessToken, idProjectUpdate, mapPutFavorite);
        int statusCode3 = apiBase.getStatusCode(resFavorite);

        assertEquals(statusCode3, 400);
    }

    @Test(description = "API: Update project - with invalid value of optional field")
    public void Test06_updateProjectWithInvalidValueOfOptionalField(){
        String accessToken = token.getToken();
        long idProjectUpdate = createProject.idProjectCreated;

        // name = ""
        Map<String, Object> mapPut1 = new HashMap<>();
        mapPut1.put("name", "");
        Response res1 = apiProject.updateProject(accessToken, idProjectUpdate, mapPut1);
        int statusCode1 = apiBase.getStatusCode(res1);
        assertEquals(statusCode1, 400);

        // favorite = null
        Map<String, Object> mapPut2 = new HashMap<>();
        mapPut2.put("favorite", null);
        Response res2 = apiProject.updateProject(accessToken, idProjectUpdate, mapPut2);
        int statusCode2 = apiBase.getStatusCode(res2);
        assertEquals(statusCode2, 400);

        // color = 56789
        Map<String, Object> mapPut3 = new HashMap<>();
        mapPut3.put("color", 56789);
        Response res3 = apiProject.updateProject(accessToken, idProjectUpdate, mapPut3);
        int statusCode3 = apiBase.getStatusCode(res3);
        assertEquals(statusCode3, 400);
    }

    @Test(description = "API: Update project - without token")
    public void Test07_updateProjectWithoutToken(){
        String accessToken = "";
        long idProjectUpdate = createProject.idProjectCreated;

        Map<String, Object> mapPut = new HashMap<>();
        mapPut.put("name", "change");

        Response res = apiProject.updateProject(accessToken, idProjectUpdate, mapPut);
        int statusCode = apiBase.getStatusCode(res);
        assertEquals(statusCode, 401);
    }

    @Test(description = "API: Update project - with non-existing token")
    public void Test08_updateProjectWithNonExistingToken(){
        String accessToken = "!@#123";
        long idProjectUpdate = createProject.idProjectCreated;

        Map<String, Object> mapPut = new HashMap<>();
        mapPut.put("name", "change");

        Response res = apiProject.updateProject(accessToken, idProjectUpdate, mapPut);
        int statusCode = apiBase.getStatusCode(res);
        assertEquals(statusCode, 401);
    }

    @Test(description = "API: Update project - with expired token")
    public void Test09_updateProjectWithExpiredToken(){
        String accessToken = tokenExpired;
        long idProjectUpdate = createProject.idProjectCreated;

        Map<String, Object> mapPut = new HashMap<>();
        mapPut.put("name", "change");

        Response res = apiProject.updateProject(accessToken, idProjectUpdate, mapPut);
        int statusCode = apiBase.getStatusCode(res);
        assertEquals(statusCode, 401);
    }

    @Test(description = "API: Update project - with empty body")
    public void Test11_updateProjectWithEmptyBody(){
        String accessToken = token.getToken();
        long idProjectUpdate = createProject.idProjectCreated;

        Map<String, Object> mapPut = new HashMap<>();

        Response res = apiProject.updateProject(accessToken, idProjectUpdate, mapPut);
        int statusCode = apiBase.getStatusCode(res);
        assertEquals(statusCode, 400);
    }
}
