package microservices.Projects.steps;

import com.google.gson.JsonObject;
import handle.HandleResponse;
import io.qameta.allure.Step;

public class Project extends HandleResponse {
    @Step("Get name of project")
    public String getNameProject(JsonObject JsonObj) {
        return getStringValueOfField("name", JsonObj);
    }

    @Step("Get url of project")
    public String getUrlProject(JsonObject JsonObj) {
        return getStringValueOfField("url", JsonObj);
    }

    @Step("Get color of project")
    public int getColorProject(JsonObject JsonObj) {
        return getIntValueOfField("color", JsonObj);
    }

    @Step("Get favorite of project")
    public Boolean getFavoriteProject(JsonObject JsonObj) {
        return getBooleanValueOfField("favorite", JsonObj);
    }

    @Step("Get order of project")
    public int getOrderProject(JsonObject JsonObj) {
        return getIntValueOfField("order", JsonObj);
    }

    @Step("Get id of project")
    public long getIdProject(JsonObject JsonObj) {
        return getlongValueOfField("id", JsonObj);
    }

    public String getStrIdProject(JsonObject JsonObj) {
        return getStringValueOfField("id", JsonObj);
    }
}
