package lambdas;

import com.google.common.base.Joiner;

import com.google.gson.Gson;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import helper.ApiGatewayHelper;
import model.Request;
import model.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import software.amazon.lambda.powertools.logging.Logging;
import software.amazon.lambda.powertools.metrics.Metrics;
import software.amazon.lambda.powertools.tracing.Tracing;

/** ServiceLambda. */
public class ServiceLambda
    implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  private static final Logger log = LogManager.getLogger(ServiceLambda.class);
  private static final ApiGatewayHelper apiGatewayHelper = new ApiGatewayHelper();

  @Logging
  @Tracing
  @Metrics(captureColdStart = true)
  @Override
  public APIGatewayProxyResponseEvent handleRequest(
      final APIGatewayProxyRequestEvent requestEvent, final Context context) {
    log.info("Received APIGatewayProxyRequestEvent: {}", requestEvent);
    Request request = (Request) apiGatewayHelper.parseRequest(requestEvent, Request.class);

    log.info("Received request: {}", request.toString());

    final String msg = Joiner.on(" ").join(request.getId(), request.getName(), "is cool!");
    log.info(msg);

    Response response =
        Response.builder().id(request.getId()).name(request.getName()).message(msg).build();

    log.info("Got response: {}", response.toString());
    return apiGatewayHelper.createResponseEvent(response, Response.class);
  }
}
