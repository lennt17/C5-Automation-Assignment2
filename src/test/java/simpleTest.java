import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.given;

public class simpleTest {
    private String postBody = "{\n" +
            "  \"name\": \"Shopping List\"\n" +
            "}";

    private String putBody = "{\n" +
            "  \"name\": \"Things To Buy\"\n" +
            "}";

    RequestSpecification request = given();

    @BeforeTest
    public static void setup() {
        RestAssured.baseURI = "https://api.todoist.com/rest/v1";
    }

    @Test
    public void Test01_postRequest() {
        basePath = "/projects";
        Response res = given()
                .contentType(ContentType.JSON)
                .headers("authorization", "Bearer " + "f15f9ca2b7d9cbe3be967b58681e9b3c0a8d1f0c")
                .and()
                .body(postBody)
                .when()
                .post()
                .then()
                .extract().response();
        System.out.println(res.prettyPrint());
    }

    @Test
    public void Test02_get_A_Project() {
        basePath = "/projects";
        final String GET_PROJECT = "/2293619235";
        Response response = given()
                .contentType(ContentType.JSON)
                .headers("authorization", "Bearer " + "f15f9ca2b7d9cbe3be967b58681e9b3c0a8d1f0c")
                .get(GET_PROJECT)
                .then()
                .extract().response();

        Object res = response.as(Object.class);
        Gson g = new Gson();
        String a = g.toJson(res);
        JsonObject j = g.fromJson(a, JsonObject.class);
        System.out.println(j);
    }

    @Test
    public void Test03_putRequest() {
        basePath = "/projects";
        final String GET_PROJECT = "/2293619235";
        Response response = given()
                .header("authorization", "Bearer " + "f15f9ca2b7d9cbe3be967b58681e9b3c0a8d1f0c")
                .contentType(ContentType.JSON)
                .and()
                .body(putBody)
                .when()
                .post(GET_PROJECT);
    }

    @Test
    public void getAllProjects() {
        // Response scope
        final String GET_ALL_PROJECTS = "/projects";
        Response response = given()
                .contentType(ContentType.JSON)
                .headers("authorization", "Bearer " + "f15f9ca2b7d9cbe3be967b58681e9b3c0a8d1f0c")
                .get(GET_ALL_PROJECTS);

        response.prettyPrint();

        List<Object> res = response.as(List.class);
        Gson g = new Gson();
        for (int i = 0; i < res.size(); i++) {
            String a = g.toJson(res.get(i));
            JsonObject j = g.fromJson(a, JsonObject.class);
            System.out.println(j.get("id"));
        }
    }
}
