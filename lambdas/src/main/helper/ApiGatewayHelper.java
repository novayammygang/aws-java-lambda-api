package helper;

import com.google.gson.Gson;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.HashMap;
import java.util.Map;

/** APIGatewayHelper. */
public class ApiGatewayHelper {

  private static final Gson gson = new Gson();

  /**
   * Generates response event.
   *
   * @return responseEvent
   */
  public APIGatewayProxyResponseEvent createResponseEvent(Object responseClass, Class clazz) {
    Map<String, String> responseHeaders = new HashMap<>();
    responseHeaders.put("Content-Type", "application/json");
    APIGatewayProxyResponseEvent response =
        new APIGatewayProxyResponseEvent()
            .withHeaders(responseHeaders)
            .withBody(gson.toJson(responseClass, clazz));
    return response;
  }

  /**
   * Parses request.
   *
   * @return responseEvent
   */
  public Object parseRequest(APIGatewayProxyRequestEvent request, Class clazz) {
    return gson.fromJson(request.getBody(), clazz);
  }
}
