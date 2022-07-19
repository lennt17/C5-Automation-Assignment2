package test;

import accessToken.Token;
import api.ApiProject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import handle.*;
import io.restassured.response.Response;
import jdk.jfr.Description;
import microservices.Projects.steps.Project;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static constant.Constant.*;
import static microservices.Projects.models.Project.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TC_CreateProject {
    Token token = new Token();
    ApiProject apiProject = new ApiProject();

    Handles handles = new Handles();
    HandleResponse handleResponse = new HandleResponse();
    Project project = new Project();
    public static long idProjectCreated;

    @Test(description = "API: Create project successfully")
    @Description("200 - Create project successfully with valid token and valid all fields")
    public void Test01_createProject() {
        String accessToken = token.getToken();
        handles.deleteAllProjects();

        String nameProject = "Shopping List";
        int colorProject = 46;
        Boolean favoriteProject = true;
        Map<String, Object> mapPost = new HashMap<>();
        mapPost.put(name, nameProject);
        mapPost.put(color, colorProject);
        mapPost.put(favorite, favoriteProject);
        //       mapPost.put(parent_id, 220330567);

        Response res = apiProject.createProject(accessToken, mapPost);
        JsonObject objProject = handleResponse.getJsonObject(res);

        int statusCode = handleResponse.getStatusCode(res);
        idProjectCreated = project.getIdProject(objProject);
        String nameProjectCreated = project.getNameProject(objProject);
        int colorProjectCreated = project.getColorProject(objProject);
        Boolean favoriteProjectCreated = project.getFavoriteProject(objProject);
        String urlProjectCreated = project.getUrlProject(objProject);
        String str_idProjectCreated = project.getStrIdProject(objProject);

        // verify status code, body, response schema
        assertEquals(statusCode, 200);
        assertEquals(nameProject, nameProjectCreated);
        assertEquals(colorProject, colorProjectCreated);
        assertEquals(favoriteProject, favoriteProjectCreated);
        assertTrue(urlProjectCreated.contains(str_idProjectCreated));
    }

    @Test(description = "API: Create project with valid token and only require field")
    @Description("200 - Create project successfully with valid token and only require field")
    public void Test02_createProjectWithRequireField() {
        String accessToken = token.getToken();

        Map<String, Object> mapPost = new HashMap<>();
        String nameProject = "ki";
        mapPost.put(name, nameProject);

        Response res = apiProject.createProject(accessToken, mapPost);
        JsonObject objProject = handleResponse.getJsonObject(res);

        int statusCode = handleResponse.getStatusCode(res);
        String nameProjectCreated = project.getNameProject(objProject);
        String urlProjectCreated = project.getUrlProject(objProject);
        String str_idProjectCreated = project.getStrIdProject(objProject);

        assertEquals(nameProjectCreated, nameProject);
        assertEquals(statusCode, 200);
        assertTrue(urlProjectCreated.contains(str_idProjectCreated));
    }

    @Test(description = "API: Create project with send request the same body twice")
    @Description("200 - Create project with send request the same body twice")
    public void Test03_createProjectWithSameBodyTwice() {
        String accessToken = token.getToken();
        handles.deleteAllProjects();

        // send request 1
        Map<String, Object> mapPost1 = new HashMap<>();
        mapPost1.put(name, "body-twice");
        Response res1 = apiProject.createProject(accessToken, mapPost1);

        int statusCode = handleResponse.getStatusCode(res1);
        JsonObject ObjectCreatedTwice = handleResponse.getJsonObject(res1);

        String nameProjectCreated = project.getNameProject(ObjectCreatedTwice);
        int orderProjectCreated = project.getOrderProject(ObjectCreatedTwice);
        String urlProjectCreated = project.getUrlProject(ObjectCreatedTwice);
        String str_idProjectCreated = project.getStrIdProject(ObjectCreatedTwice);

        assertEquals(statusCode, 200);
        assertEquals(nameProjectCreated, "body-twice");
        assertEquals(orderProjectCreated, 1);
        assertTrue(urlProjectCreated.contains(str_idProjectCreated));

        // send request 2
        Map<String, Object> mapPost2 = new HashMap<>();
        mapPost2.put(name, "body-twice");
        Response res2 = apiProject.createProject(accessToken, mapPost2);

        int statusCode2 = handleResponse.getStatusCode(res2);
        JsonObject ObjectCreatedTwice2 = handleResponse.getJsonObject(res2);

        String nameProjectCreated2 = project.getNameProject(ObjectCreatedTwice2);
        int orderProjectCreated2 = project.getOrderProject(ObjectCreatedTwice2);
        String urlProjectCreated2 = project.getUrlProject(ObjectCreatedTwice2);
        String str_idProjectCreated2 = project.getStrIdProject(ObjectCreatedTwice2);

        assertEquals(statusCode2, 200);
        assertEquals(nameProjectCreated2, "body-twice");
        assertEquals(orderProjectCreated2, 2);
        assertTrue(urlProjectCreated2.contains(str_idProjectCreated2));
    }

    @Test(description = "API: Create project with invalid type of field name")
    @Description("400 - Create project with invalid type of field name")
    public void Test04_createProjectWithInvalidTypeOfName() {
        String accessToken = token.getToken();

        // name = integer
        Map<String, Object> mapPost = new HashMap<>();
        mapPost.put(name, 123);

        Response res = apiProject.createProject(accessToken, mapPost);
        int statusCode = handleResponse.getStatusCode(res);

        assertEquals(statusCode, 400);

        // name = Boolean
        Map<String, Object> mapPost2 = new HashMap<>();
        mapPost2.put(name, true);

        Response re = apiProject.createProject(accessToken, mapPost2);
        int statusCode2 = handleResponse.getStatusCode(re);

        assertEquals(statusCode2, 400);
    }

    @Test(description = "API: Create project without name")
    @Description("400 - Create project without name")
    public void Test05_createProjectWithoutName() {
        String accessToken = token.getToken();

        // name = ""
        Map<String, Object> mapPost1 = new HashMap<>();
        mapPost1.put(name, "");

        Response res = apiProject.createProject(accessToken, mapPost1);

        int statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 400);

        // name = null
        Map<String, Object> mapPost2 = new HashMap<>();
        mapPost2.put(name, null);

        Response re = apiProject.createProject(accessToken, mapPost2);

        int statusCode2 = handleResponse.getStatusCode(re);
        assertEquals(statusCode2, 400);

        // do not send name
        Map<String, Object> mapPost3 = new HashMap<>();
        mapPost3.put(color, 45);
        mapPost3.put(favorite, false);

        Response r = apiProject.createProject(accessToken, mapPost3);

        int statusCode3 = handleResponse.getStatusCode(r);
        assertEquals(statusCode3, 400);
    }

    @Test(description = "API: Create project with invalid value of optional field")
    @Description("400 - Create project with invalid value of optional field")
    public void Test06_createProjectWithInvalidValueOfOptionalFields() {
        String accessToken = token.getToken();

        // parent_id = non-existing id
        Map<String, Object> mapPost1 = new HashMap<>();
        mapPost1.put(name, "invalid value of optional fields");
        mapPost1.put(parent_id, 12389);

        Response res1 = apiProject.createProject(accessToken, mapPost1);
        int statusCode1 = handleResponse.getStatusCode(res1);
        assertEquals(statusCode1, 400);

        // favorite = null
        Map<String, Object> mapPost2 = new HashMap<>();
        mapPost1.put(name, "invalid value of optional fields");
        mapPost1.put("favorite", null);

        Response res2 = apiProject.createProject(accessToken, mapPost2);
        int statusCode2 = handleResponse.getStatusCode(res2);
        assertEquals(statusCode2, 400);
    }

    @Test(description = "API: Create project with invalid type of optional field")
    @Description("400 - Create project with invalid type of optional field")
    public void Test07_createProjectWithInvalidTypeOfOptionalFields() {
        String accessToken = token.getToken();

        // favorite = integer
        Map<String, Object> mapPost = new HashMap<>();
        mapPost.put(name, "invalid type of optional fields");
        mapPost.put(favorite, 123);

        Response res = apiProject.createProject(accessToken, mapPost);
        int statusCode = handleResponse.getStatusCode(res);

        assertEquals(statusCode, 400);

        // parent_id = String
        Map<String, Object> mapPost2 = new HashMap<>();
        mapPost2.put(name, "invalid type of optional fields");
        mapPost2.put(parent_id, "2203387945");

        Response re = apiProject.createProject(accessToken, mapPost2);
        statusCode = handleResponse.getStatusCode(re);

        assertEquals(statusCode, 400);
    }

    @Test(description = "API: Create project with undefine field")
    @Description("400 - Create project with undefine field")
    public void Test08_createProjectWithUndefineField() {
        String accessToken = token.getToken();

        Map<String, Object> mapPost = new HashMap<>();
        mapPost.put(name, "undefine field");
        mapPost.put("constant", 34);

        Response res = apiProject.createProject(accessToken, mapPost);
        int statusCode = handleResponse.getStatusCode(res);

        assertEquals(statusCode, 400);
    }

    @Test(description = "API: Create project with empty body")
    @Description("400 - Create project with empty body")
    public void Test09_createProjectWithEmptyBody() {
        String accessToken = token.getToken();

        Map<String, Object> mapPost = new HashMap<>();

        Response res = apiProject.createProject(accessToken, mapPost);

        int statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 400);
    }

    @Test(description = "API: Create project without token")
    @Description("401 - Create project without token")
    public void Test10_createProjectWithoutToken() {
        String accessToken = "";

        Map<String, Object> mapPost = new HashMap<>();
        String nameProject = "without token";
        mapPost.put(name, nameProject);

        Response res = apiProject.createProject(accessToken, mapPost);

        int statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 401);
    }

    @Test(description = "API: Create project with non-existing token")
    @Description("401 - Create project with non-existing token")
    public void Test11_createProjectWithNonExistingToken() {
        String accessToken = "!@#";

        Map<String, Object> mapPost = new HashMap<>();
        String nameProject = "non-exist token";
        mapPost.put(name, nameProject);

        Response res = apiProject.createProject(accessToken, mapPost);

        int statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 401);
    }

    @Test(description = "API: Create project with expired token")
    @Description("401 - Create project with expired token")
    public void Test12_createProjectWithExpiredToken() {
        String accessToken = tokenExpired;

        Map<String, Object> mapPost = new HashMap<>();
        mapPost.put(name, "expired token");

        Response res = apiProject.createProject(accessToken, mapPost);
        int statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 401);
    }

    @Test(description = "API: Create project with different method")
    @Description("405 - Create project with different method")
    public void Test13_createProjectWithDifferentMethod() {
        String accessToken = token.getToken();

        Response res = apiProject.createProjectWithGetMethod(accessToken);
        int statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 405);
    }

    @Test(description = "API: Create project when reached maximum projects")
    @Description("403 - Create project when reached maximum projects")
    public void Test14_createProjectWhenReachedMaximumProjects() {
        String accessToken = token.getToken();

        Response r = apiProject.getAllProjects(accessToken);
        JsonArray arr = handleResponse.getJsonArray(r);
        for (int i = 0; i < 7; i++) {
            if (arr.size() < 8) {
                Map<String, Object> mapPost = new HashMap<>();
                mapPost.put(name, "create");

                apiProject.createProject(accessToken, mapPost);
            } else if (arr.size() == 8) {
                Map<String, Object> mapPost = new HashMap<>();
                mapPost.put(name, "maximum projects");

                // available exist 8 projects (includes Inbox project) in this account
                Response res = apiProject.createProject(accessToken, mapPost);
                int statusCode = handleResponse.getStatusCode(res);
                assertEquals(statusCode, 403);
            }
        }
    }
}
