package accessToken;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static constant.Endpoint.*;
import static io.restassured.RestAssured.given;

import api.APIBase;

public class Token extends APIBase {
    Gson g = new Gson();
    String accessToken;
    Map<String, Object> mapLogin = new HashMap<>();

    public String getToken() {
        RestAssured.baseURI = baseURIToken;
        RestAssured.basePath = "";
        String email = configSettings.getEmail();
        String password = configSettings.getPassword();
        mapLogin.put("email", email);
        mapLogin.put("password", password);

        Response res = given()
                .contentType(ContentType.JSON)
                .and()
                .body(mapLogin)
                .when()
                .post()
                .then()
                .extract().response();

        Object response = res.as(Object.class);
        String a = g.toJson(response);
        JsonObject j = g.fromJson(a, JsonObject.class);
        return accessToken = (j.get("token")).getAsString();
    }
}
