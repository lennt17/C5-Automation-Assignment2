package handle;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.response.Response;

public class HandleResponse {
    Gson g = new Gson();

    public JsonObject getJsonObject(Response re) {
        Object res = re.as(Object.class);
        String a = g.toJson(res);
        return g.fromJson(a, JsonObject.class);
    }

    public JsonArray getJsonArray(Response re) {
        Object res = re.as(Object.class);
        String a = g.toJson(res);
        return g.fromJson(a, JsonArray.class);
    }

    public int getStatusCode(Response response) {
        return response.getStatusCode();
    }

    public String getStringValueOfField(String fieldName, JsonObject obj) {
        return obj.get(fieldName).getAsString();
    }

    public int getIntValueOfField(String fieldName, JsonObject obj) {
        return obj.get(fieldName).getAsInt();
    }

    public long getlongValueOfField(String fieldName, JsonObject obj) {
        return obj.get(fieldName).getAsLong();
    }

    public Boolean getBooleanValueOfField(String fieldName, JsonObject obj) {
        return obj.get(fieldName).getAsBoolean();
    }
}
