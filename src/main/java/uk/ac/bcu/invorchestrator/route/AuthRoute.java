package uk.ac.bcu.invorchestrator.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import uk.ac.bcu.invorchestrator.dto.DataResponse;
import uk.ac.bcu.invorchestrator.dto.auth.SignInRequest;
import uk.ac.bcu.invorchestrator.dto.auth.SignUpRequest;
import uk.ac.bcu.invorchestrator.dto.auth.UpdateClientDTO;
import uk.ac.bcu.invorchestrator.service.AuthService;

@Component
public class AuthRoute extends RouteBuilder {

    private final AuthService authService;


    public AuthRoute(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Handle Associated business process relevant to
     * @throws Exception Exception from process
     */
    private void handleAuthBusinessProcess() throws Exception {
        authService.handleSignIn(from("direct:sign-in"));
        authService.handleSignUp(from("direct:sign-up"));
    }

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .enableCORS(true)
                .component("servlet")
                .bindingMode(RestBindingMode.json)

                //Enable swagger endpoint.
                .apiContextPath("/swagger") //swagger endpoint path
                .apiContextRouteId("swagger");

        // Define endpoints and reference to request handlers
        rest()
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .post("/auth/sign-in").type(SignInRequest.class).to("direct:sign-in")
                .post("/auth/sign-up").type(SignUpRequest.class).to("direct:sign-up")
                .outType(DataResponse.class);

        this.handleAuthBusinessProcess();
    }
}
