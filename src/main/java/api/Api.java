package api;

import WebKeywords.WebKeywords;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import variable.Token;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.given;

public class Api {
    public WebKeywords action;
    Gson g = new Gson();
    String str_id_project;
    String str_id_task;
    String accessToken;
    public Token token;
    Map<String, Object> mapProject = new HashMap<>();
    Map<String, Object> mapTask = new HashMap<>();

    public String getAccessToken(){
        token = new Token(action);
        return accessToken = token.getToken();
    }

    public String createProject(String nameProject) {
        accessToken = getAccessToken();
        System.out.println("before createProject: " + accessToken);
        RestAssured.baseURI = "https://api.todoist.com/rest/v1/";
        basePath = "projects";
        mapProject.put("name", nameProject);
        Response re = given()
                .header("authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .when()
                .body(mapProject)
                .post();
        re.prettyPrint();

        Object res = re.as(Object.class);
        String a = g.toJson(res);
        JsonObject k = g.fromJson(a, JsonObject.class);
        return str_id_project = (k.get("id")).toString();
    }

    public String createTask(String nameTask, String idProjectCreated) {
        RestAssured.baseURI = "https://api.todoist.com/rest/v1/";
        basePath = "/tasks";
        mapTask.put("project_id", idProjectCreated);
        mapTask.put("content", nameTask);
        mapTask.put("due_string", "tomorrow at 13:00");
        mapTask.put("due_lang", "en");
        mapTask.put("priority", 4);

        Response responTask = given()
                .header("authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .when()
                .body(mapTask)
                .post();
        responTask.prettyPrint();

        Object responT = responTask.as(Object.class);
        String resTask = g.toJson(responT);
        JsonObject k = g.fromJson(resTask, JsonObject.class);
        return str_id_task = (k.get("id")).toString();
    }

    public void reOpenTask(String str_id_task) {
        RestAssured.baseURI = "https://api.todoist.com/rest/v1/";
        basePath = "/tasks/" + str_id_task;
        final String REOPEN = "/reopen";
        Response resp = given()
                .header("authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .post(REOPEN);
        resp.prettyPrint();
    }
}
