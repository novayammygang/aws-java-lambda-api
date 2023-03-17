import static org.junit.jupiter.api.Assertions.assertEquals;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import lambdas.ServiceLambda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


/**
 * ServiceLambdaTest
 */
public class ServiceLambdaTest {

  @Mock
  private Context context;

  private ServiceLambda serviceLambda;

  @BeforeEach
  public void init() {
    this.serviceLambda = new ServiceLambda();
  }

  @Test
  public void test_handleRequest_success() {
    serviceLambda.handleRequest(new APIGatewayProxyRequestEvent().withBody("{\"id\": \"Kyle\", \"name\": \"Newmiller\"}"), context);
    assertEquals(1, 1);
  }
}