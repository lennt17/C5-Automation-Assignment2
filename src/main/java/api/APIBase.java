package api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.configs.ConfigSettings;

import java.util.Map;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.given;
import static constant.Constant.*;

public class APIBase {
    Gson g = new Gson();
    public ConfigSettings configSettings;
    public APIBase(){
        configSettings = new ConfigSettings(System.getProperty("user.dir"));
    }

    public Response sendPost(String accessToken, String basePathPT, Map mapPost) {
        RestAssured.baseURI = baseURI;
        basePath = basePathPT;
        Response res = given()
                .header("authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .when()
                .body(mapPost)
                .post();

        return res;
    }

    public Response sendGet(String accessToken, String basePathPT, long id) {
        RestAssured.baseURI = baseURI;
        basePath = basePathPT;
        final String GET = "/" + id;
        Response res = given()
                .contentType(ContentType.JSON)
                .headers("authorization", "Bearer " + accessToken)
                .get(GET);

        return res;
    }

    public Response sendGet(String accessToken, String basePathPT) {
        RestAssured.baseURI = baseURI;
        basePath = basePathPT;
        final String GET = "/";
        Response res = given()
                .contentType(ContentType.JSON)
                .headers("authorization", "Bearer " + accessToken)
                .get(GET);

        return res;
    }

    public Response sendPostReopen(String accessToken, String basePathPT, long id) {
        RestAssured.baseURI = baseURI;
        basePath = basePathPT + "/" + id;
        final String REOPEN = "/reopen";
        Response res = given()
                .header("authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .post(REOPEN);

        return res;
    }

    public Response sendDelete(String accessToken, String basePathPT){
        RestAssured.baseURI = baseURI;
        basePath = basePathPT;
        Response res = given()
                .contentType(ContentType.JSON)
                .headers("authorization", "Bearer " + accessToken)
                .delete();

        return res;
    }

    public JsonObject getJsonObject(Response re){
        Object res = re.as(Object.class);
        String a = g.toJson(res);
        return g.fromJson(a, JsonObject.class);
    }

    public JsonArray getJsonArray(Response re){
        Object res = re.as(Object.class);
        String a = g.toJson(res);
        return g.fromJson(a, JsonArray.class);
    }

    public int getStatusCode(Response response){
        return response.getStatusCode();
    }

}
