package variable;

import WebKeywords.WebKeywords;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class Token {
    public WebKeywords action;
    public String accessToken;

    Gson g = new Gson();

    Map<String, Object> mapLogin = new HashMap<>();
    public Token(WebKeywords action){
        this.action = action;
    }

    public String getToken(){
        RestAssured.baseURI = "https://todoist.com/API/v8.7/user/login";
        mapLogin.put("email", "lennt2k@gmail.com");
        mapLogin.put("password", "Len181403032");
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
