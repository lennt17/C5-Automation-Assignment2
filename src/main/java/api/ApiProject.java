package api;
import io.restassured.response.Response;

import java.util.Map;
import static constant.Endpoint.*;

public class ApiProject extends APIBase {

    public Response createProject(String accessToken, Map mapPost){
        return sendPost(accessToken, basePath_project, mapPost);
    }

    public Response createProjectWithGetMethod(String accessToken){
        return sendGet(accessToken, basePath_project);
    }

    public Response getAProject(String accessToken, long idProjectGet){
        return sendGet(accessToken, basePath_project, idProjectGet);
    }

    public Response getAProjectWithPostMethod(String accessToken,long idProjectGet, Map mapPost){
        String basePathProject = basePath_project + "/" + idProjectGet;
        return sendPost(accessToken, basePathProject, mapPost);
    }

    public Response getAllProjects(String accessToken){
        return sendGet(accessToken, basePath_project);
    }

    public Response getAllProjectsWithPostMethod(String accessToken, Map mapPost){
        return sendPost(accessToken, basePath_project, mapPost);
    }


    public Response updateProject(String accessToken, long idProject, Map mapUpdate){
        String basePathUpdate = basePath_project + "/" + idProject;
        return sendPost(accessToken, basePathUpdate ,mapUpdate);
    }

    public Response updateProjectWithGetMethod(String accessToken, long idProject, Map mapUpdate){
        String basePathUpdate = basePath_project + "/" + idProject;
        return sendGet(accessToken, basePathUpdate);
    }

    public Response deleteProject(String accessToken, long idProject){
        String basePathDelete = basePath_project;
        return sendDelete(accessToken, basePathDelete, idProject);
    }
}
