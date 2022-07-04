package variable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.configs.ConfigSettings;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class Token {
    public String accessToken;

    public ConfigSettings configSettings;

    Gson g = new Gson();

    Map<String, Object> mapLogin = new HashMap<>();
    public Token(){
        configSettings = new ConfigSettings(System.getProperty("user.dir"));
    }

    public String getToken(){
        RestAssured.baseURI = configSettings.getBaseURIToken();
        String email = configSettings.getEmail();
        String password =configSettings.getPassword();
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
        res.prettyPrint();
        Object response = res.as(Object.class);
        String a = g.toJson(response);
        JsonObject j = g.fromJson(a, JsonObject.class);
        return accessToken = (j.get("token")).getAsString();
    }
}
