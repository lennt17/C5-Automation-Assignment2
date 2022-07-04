package api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import utils.configs.ConfigSettings;

import java.util.Map;

public class ApiTask extends APIBase {
    public ConfigSettings configSettings;
    public APIBase apiBase;
    public JsonObject ObjectCreated;
    String basePathTask;

    Gson g = new Gson();
    public ApiTask(){
        configSettings = new ConfigSettings(System.getProperty("user.dir"));
    }

    public JsonObject create(String accessToken, Map mapPost){
        apiBase = new APIBase();
        basePathTask = configSettings.getBasePathTask();
        return ObjectCreated = apiBase.sendPost(accessToken, basePathTask, mapPost);
    }

    public void reOpenTask(String accessToken, String str_id_task) {
        basePathTask = configSettings.getBasePathTask();
        apiBase.sendPostReopen(accessToken,basePathTask, str_id_task);
    }
}
