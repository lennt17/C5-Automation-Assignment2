package test;

import accessToken.Token;
import api.APIBase;
import api.ApiProject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TC_DeleteProject {
    ApiProject apiProject = new ApiProject();
    APIBase apiBase = new APIBase();
    Token token = new Token();

    @Test(description = "API: Delete all projects - successfully")
    public void TC01_DeleteAllProjects(){
        String accessToken = token.getToken();

        Response re = apiProject.getAllProjects(accessToken);
        JsonArray arr = apiBase.getJsonArray(re);
        for(int i = 0; i < arr.size(); i++){
            JsonObject objectProject = ((arr.get(i))).getAsJsonObject();
            Long idProject = objectProject.get("id").getAsLong();
            System.out.println("id: " + idProject);

            Response res = apiProject.deleteProject(accessToken, idProject);
            int statusCode = apiBase.getStatusCode(res);
            assertEquals(statusCode, 204);
        }
    }
}
