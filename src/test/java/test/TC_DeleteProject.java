package test;

import accessToken.Token;

import api.ApiProject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import handle.HandleResponse;
import io.restassured.response.Response;
import microservices.Projects.steps.Project;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TC_DeleteProject {
    ApiProject apiProject = new ApiProject();
    Project project = new Project();
    HandleResponse handleResponse = new HandleResponse();
    Token token = new Token();

    @Test(description = "API: Delete all projects - successfully")
    public void TC01_DeleteAllProjects() {
        String accessToken = token.getToken();

        Response re = apiProject.getAllProjects(accessToken);
        JsonArray arr = handleResponse.getJsonArray(re);
        for (int i = 0; i < arr.size(); i++) {
            JsonObject objProject = (arr.get(i)).getAsJsonObject();
            long idProject = project.getIdProject(objProject);
            System.out.println("Delete idProject: " + idProject);

            Response res = apiProject.deleteProject(accessToken, idProject);
            int statusCode = handleResponse.getStatusCode(res);
            assertEquals(statusCode, 204);
        }

        Response res = apiProject.getAllProjects(accessToken);
        JsonArray arrayProject = handleResponse.getJsonArray(res);
        int numberOfProject = arrayProject.size();
        assertEquals(numberOfProject, 0);
    }
}
