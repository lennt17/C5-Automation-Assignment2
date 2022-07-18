package api;
import io.restassured.response.Response;

import java.util.Map;
import static constant.Endpoint.*;

public class ApiProject extends APIBase {
    String basePathProject = basePath_project;

    public Response createProject(String accessToken, Map mapPost){
        return sendPost(accessToken, basePathProject, mapPost);
    }

    public Response getAProject(String accessToken, long idProjectGet){
        return sendGet(accessToken, basePathProject, idProjectGet);
    }

    public Response getAllProjects(String accessToken){
        return sendGet(accessToken, basePathProject);
    }

    public Response updateProject(String accessToken, long idProject, Map mapUpdate){
        String basePathUpdate = basePathProject + "/" + idProject;
        return sendPost(accessToken, basePathUpdate ,mapUpdate);
    }

    public Response deleteProject(String accessToken, long idProject){
        String basePathDelete = basePathProject;
        return sendDelete(accessToken, basePathDelete, idProject);
    }
}
