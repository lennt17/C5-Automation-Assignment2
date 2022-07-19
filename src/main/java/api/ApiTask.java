package api;

import java.util.Map;

import static constant.Endpoint.*;

import io.restassured.response.Response;

public class ApiTask extends APIBase {
    public Response createTask(String accessToken, Map mapPost) {
        return sendPost(accessToken, basePath_task, mapPost);
    }

    public Response reOpenTask(String accessToken, long str_id_task) {
        return sendPostReopen(accessToken, basePath_task, str_id_task);
    }
}
