package cdk;

import software.amazon.awscdk.core.App;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.Method;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.lambda.LayerVersionProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.logs.RetentionDays;

import java.util.Arrays;

/** LambdaServiceStack . */
public class LambdaServiceStack extends software.amazon.awscdk.core.Stack {

  /** LambdaServiceStack. */
  public LambdaServiceStack(final Construct scope, final String id) {
    this(scope, id, null);
  }

  /** LambdaServiceStack. */
  public LambdaServiceStack(final Construct scope, final String id, final StackProps props) {
    super(scope, id, props);

    this.createTemplateLambda();
  }

  /** LambdaServiceStack main. */
  public static void main(final String[] args) {
    final App app = new App();
    new LambdaServiceStack(app, "LambdaServiceStack", StackProps.builder().build());
    app.synth();
  }

  private void createTemplateLambda() {
    // Create a layer from the layer module
    final LayerVersion layer =
        new LayerVersion(
            this,
            "layer",
            LayerVersionProps.builder()
                .code(Code.fromAsset("../layer/target/bundle"))
                .compatibleRuntimes(Arrays.asList(Runtime.JAVA_8))
                .build());

    // The code that defines your stack goes here
    Function templateHandler =
        new Function(
            this,
            "TemplateLambda",
            FunctionProps.builder()
                .functionName("TemplateLambda")
                .runtime(Runtime.JAVA_8)
                .code(Code.fromAsset("../lambdas/target/lambdas.jar"))
                .handler("lambdas.ServiceLambda")
                .layers(Arrays.asList(layer))
                .memorySize(1024)
                .timeout(Duration.seconds(30))
                .logRetention(RetentionDays.ONE_WEEK)
                .build());

    RestApi api =
        RestApi.Builder.create(this, "Template-API")
            .restApiName("Template Service")
            .description("This service services template APIs.")
            .build();

    LambdaIntegration getTemplateIntegration =
        LambdaIntegration.Builder.create(templateHandler)
            .requestTemplates(
                java.util.Map.of( // Map.of is Java 9 or later
                    "application/json", "{ \"statusCode\": \"200\" }"))
            .build();

    api.getRoot().addMethod("GET", getTemplateIntegration);

    Resource getTemplateIntegrationResource = api.getRoot().addResource("template");
    Method getTemplateIntegrationMethod =
        getTemplateIntegrationResource.addMethod("POST", getTemplateIntegration);
  }
}
