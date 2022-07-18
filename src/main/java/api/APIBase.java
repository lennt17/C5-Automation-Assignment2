package api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.configs.ConfigSettings;

import java.util.Map;

import static constant.Endpoint.*;
import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.given;

public class APIBase {
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

    public Response sendDelete(String accessToken, String basePathPT, long id){
        RestAssured.baseURI = baseURI;
        basePath = basePathPT + "/" + id;
        Response res = given()
                .contentType(ContentType.JSON)
                .headers("authorization", "Bearer " + accessToken)
                .delete();

        return res;
    }
}
