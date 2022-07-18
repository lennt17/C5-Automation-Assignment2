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

import static constant.Constant.*;
import static microservices.Projects.models.Project.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TC_CreateProject{
    Token token = new Token();
    ApiProject apiProject = new ApiProject();
    HandleResponse handleResponse = new HandleResponse();
    Project project = new Project();
    public static long idProjectCreated;
    Long idProjectCreate;
    String nameProjectCreated;
    int colorProjectCreated;
    String urlProjectCreated;
    Boolean favoriteProjectCreated;
    int orderProjectCreated;
    int statusCode;

    @Test(description = "API: Create project - Create successfully with valid token and valid all fields")
    public void Test01_createProject() {
        String accessToken = token.getToken();

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

        statusCode = handleResponse.getStatusCode(res);
        idProjectCreated = project.getIdProject(objProject);
        nameProjectCreated = project.getNameProject(objProject);
        colorProjectCreated = project.getColorProject(objProject);
        favoriteProjectCreated = project.getFavoriteProject(objProject);
        urlProjectCreated = project.getUrlProject(objProject);
        String str_idProjectCreated = project.getStrIdProject(objProject);

        // verify status code, body, response schema
        assertEquals(statusCode, 200);
        assertEquals(nameProject, nameProjectCreated);
        assertEquals(colorProject, colorProjectCreated);
        assertEquals(favoriteProject, favoriteProjectCreated);
        assertTrue(urlProjectCreated.contains(str_idProjectCreated));
    }

    @Test(description = "API: Create project - valid token and only require field")
    public void Test02_createProjectWithRequireField(){
        String accessToken = token.getToken();

        Map<String, Object> mapPost = new HashMap<>();
        String nameProject = "ki";
        mapPost.put(name, nameProject);

        Response res = apiProject.createProject(accessToken, mapPost);
        JsonObject objProject = handleResponse.getJsonObject(res);

        statusCode = handleResponse.getStatusCode(res);
        nameProjectCreated = project.getNameProject(objProject);
        urlProjectCreated = project.getUrlProject(objProject);
        String str_idProjectCreated = project.getStrIdProject(objProject);

        assertEquals(nameProjectCreated, nameProject);
        assertEquals(statusCode, 200);
        assertTrue(urlProjectCreated.contains(str_idProjectCreated));
    }

    @Test(description = "API: Create project - send request with the same body twice")
    public void Test03_createProjectWithSameBodyTwice(){
        String accessToken = token.getToken();

        // in the before testcase, already exist project with name "ki", it was executed in test02
        Map<String, Object> mapPost = new HashMap<>();
        mapPost.put(name, "ki");
        Response res = apiProject.createProject(accessToken, mapPost);

        statusCode = handleResponse.getStatusCode(res);
        JsonObject ObjectCreatedTwice = handleResponse.getJsonObject(res);

        idProjectCreate = project.getIdProject(ObjectCreatedTwice);
        nameProjectCreated = project.getNameProject(ObjectCreatedTwice);
        orderProjectCreated = project.getOrderProject(ObjectCreatedTwice);
        urlProjectCreated = project.getUrlProject(ObjectCreatedTwice);
        String str_idProjectCreated = project.getStrIdProject(ObjectCreatedTwice);

        assertEquals(statusCode, 200);
        assertEquals(nameProjectCreated, "ki");
        assertEquals(orderProjectCreated, 3);
        assertTrue(urlProjectCreated.contains(str_idProjectCreated));
    }

    @Test(description = "API: Create project - invalid of type field name")
    public void Test04_createProjectWithInvalidTypeOfName(){
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

    @Test(description = "API: Create project - without name")
    public void Test05_createProjectWithoutName(){
        String accessToken = token.getToken();

        // name = ""
        Map<String, Object> mapPost1 = new HashMap<>();
        mapPost1.put(name, "");

        Response res = apiProject.createProject(accessToken, mapPost1);

        statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 400);

        // name = null
        Map<String, Object> mapPost2 = new HashMap<>();
        mapPost2.put(name, null);

        Response re = apiProject.createProject(accessToken, mapPost2);

        int statusCode2 = handleResponse.getStatusCode(re);
        assertEquals(statusCode2, 400);

        // do not send name
        Map<String, Object> mapPost3 = new HashMap<>();
        mapPost3.put("color", 45);
        mapPost3.put("favorite", false);

        Response r = apiProject.createProject(accessToken, mapPost3);

        int statusCode3 = handleResponse.getStatusCode(r);
        assertEquals(statusCode3, 400);
    }

    @Test(description = "API: Create project - invalid value of optional field")
    public void Test06_createProjectWithInvalidValueOfOptionalFields(){
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

    @Test(description = "API: Create project - invalid type of optional field")
    public void Test07_createProjectWithInvalidTypeOfOptionalFields(){
        String accessToken = token.getToken();

        // favorite = integer
        Map<String, Object> mapPost = new HashMap<>();
        mapPost.put(name, "invalid type of optional fields");
        mapPost.put(favorite, 123);

        Response res = apiProject.createProject(accessToken, mapPost);
        statusCode = handleResponse.getStatusCode(res);

        assertEquals(statusCode, 400);

        // parent_id = String
        Map<String, Object> mapPost2 = new HashMap<>();
        mapPost2.put(name, "invalid type of optional fields");
        mapPost2.put(parent_id, "2203387945");

        Response re = apiProject.createProject(accessToken, mapPost2);
        statusCode = handleResponse.getStatusCode(re);

        assertEquals(statusCode, 400);
    }

    @Test(description = "API: Create project - undefine field")
    public void Test08_createProjectWithUndefineField(){
        String accessToken = token.getToken();

        Map<String, Object> mapPost = new HashMap<>();
        mapPost.put(name, "undefine field");
        mapPost.put("constant", 34);

        Response res = apiProject.createProject(accessToken, mapPost);
        statusCode = handleResponse.getStatusCode(res);

        assertEquals(statusCode, 400);
    }

    @Test(description = "API: Create project - empty body")
    public void Test09_createProjectWithEmptyBody(){
        String accessToken = token.getToken();

        Map<String, Object> mapPost = new HashMap<>();

        Response res = apiProject.createProject(accessToken, mapPost);

        statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 400);
    }

    @Test(description = "API: Create project - without token")
    public void Test10_createProjectWithoutToken(){
        String accessToken = "";

        Map<String, Object> mapPost = new HashMap<>();
        String nameProject = "without token";
        mapPost.put(name, nameProject);

        Response res = apiProject.createProject(accessToken, mapPost);

        statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 401);
    }

    @Test(description = "API: Create project - non-existing token")
    public void Test11_createProjectWithNonExistingToken(){
        String accessToken = "!@#";

        Map<String, Object> mapPost = new HashMap<>();
        String nameProject = "non-exist token";
        mapPost.put(name, nameProject);

        Response res = apiProject.createProject(accessToken, mapPost);

        statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 401);
    }

    @Test(description = "API: Create project - expired token")
    public void Test12_createProjectWithExpiredToken(){
        String accessToken = tokenExpired;

        Map<String, Object> mapPost = new HashMap<>();
        mapPost.put(name, "expired token");

        Response res = apiProject.createProject(accessToken, mapPost);
        statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 401);
    }

    @Test(description = "API: Create project - when reached maximum projects")
    public void Test14_createProjectWhenReachedMaximumProjects(){
        String accessToken = token.getToken();
        Map<String, Object> mapPost = new HashMap<>();
        mapPost.put(name, "maximum projects");

        // before available exist 7 projects in this account
        Response res = apiProject.createProject(accessToken, mapPost);
        statusCode = handleResponse.getStatusCode(res);
        assertEquals(statusCode, 403);
    }
}
