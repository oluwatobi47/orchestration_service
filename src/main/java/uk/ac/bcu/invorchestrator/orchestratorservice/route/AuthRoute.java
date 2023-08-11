package uk.ac.bcu.invorchestrator.orchestratorservice.route;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import uk.ac.bcu.invorchestrator.orchestratorservice.dto.DataResponse;
import uk.ac.bcu.invorchestrator.orchestratorservice.service.AuthService;

@Component
public class AuthRoute extends RouteBuilder {

    private final AuthService authService;


    public AuthRoute(AuthService authService) {
        this.authService = authService;
    }


    /**
     * Handle Associated business process relevant to
     * @throws Exception
     */
    private void handleAuthBusinessProcess() throws Exception {
        var signInConsumer = from("direct:sign-in")
                .log(LoggingLevel.DEBUG, "Processing ${body}");
        authService.handleSignIn(signInConsumer);
        from("direct:sign-out").process((authService::signOut));
        from("direct:sign-up").process((authService::signUp));
        from("direct:get-client-info").process((authService::getClientInformation));
    }

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.auto);


        // Define endpoints and reference to request handlers
        rest()
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .post("/auth/sign-in").to("direct:sign-in")
                .post("/auth/sign-out").to("direct:sign-out")
                .post("/auth/sign-up").to("direct:sign-up")
                .get("/auth/client-info/{clientId}").to("direct:get-client-info")
                .outType(DataResponse.class);

        this.handleAuthBusinessProcess();
    }
}
