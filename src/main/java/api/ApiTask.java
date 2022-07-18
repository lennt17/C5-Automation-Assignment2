package api;

import java.util.Map;
import static constant.Endpoint.*;

import io.restassured.response.Response;

public class ApiTask extends APIBase {
    String basePathTask = basePath_task;

    public Response createTask(String accessToken, Map mapPost){
        return sendPost(accessToken, basePathTask, mapPost);
    }

    public Response reOpenTask(String accessToken, long str_id_task) {
        return sendPostReopen(accessToken,basePathTask, str_id_task);
    }
}
