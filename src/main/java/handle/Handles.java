package handle;

import accessToken.Token;
import api.ApiProject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.response.Response;
import microservices.Projects.steps.Project;

public class Handles {
    Token token = new Token();
    ApiProject  apiProject = new ApiProject();
    HandleResponse handleResponse = new HandleResponse();
    Project project = new Project();

    public void deleteAllProjects(){
        String accessToken = token.getToken();

        Response re = apiProject.getAllProjects(accessToken);
        JsonArray arr = handleResponse.getJsonArray(re);
        for (int i = 0; i < arr.size(); i++) {
            JsonObject objProject = (arr.get(i)).getAsJsonObject();
            long idProject = project.getIdProject(objProject);

            apiProject.deleteProject(accessToken, idProject);
        }
    }
}
