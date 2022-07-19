package microservices.Tasks.steps;

import com.google.gson.JsonObject;
import handle.HandleResponse;
import io.qameta.allure.Step;

public class Tasks extends HandleResponse {
    @Step("Get id of task")
    public long getIdTask(JsonObject JsonObj){
        return getlongValueOfField("id", JsonObj);
    }
}
