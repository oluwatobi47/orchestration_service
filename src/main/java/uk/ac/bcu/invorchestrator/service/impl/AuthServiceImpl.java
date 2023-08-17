package uk.ac.bcu.invorchestrator.service.impl;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.http.HttpMethods;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Service;
import uk.ac.bcu.invorchestrator.config.ServiceEndpoints;
import uk.ac.bcu.invorchestrator.dto.DataResponse;
import uk.ac.bcu.invorchestrator.dto.NotificationRequest;
import uk.ac.bcu.invorchestrator.dto.auth.AuthResponse;
import uk.ac.bcu.invorchestrator.dto.auth.Client;
import uk.ac.bcu.invorchestrator.dto.auth.SignInResponse;
import uk.ac.bcu.invorchestrator.service.AuthService;
import uk.ac.bcu.invorchestrator.service.ResponseTransformer;
import uk.ac.bcu.invorchestrator.service.SessionContext;

import java.text.MessageFormat;

@Service
public class AuthServiceImpl implements AuthService {

    private final ServiceEndpoints serviceEndpoint;

    private final SessionContext sessionContext;

    private final ExceptionHandler exceptionHandler;

    private final ResponseTransformer<AuthResponse> responseTransformer;

    public AuthServiceImpl(ServiceEndpoints endpoints, SessionContext sessionContext, ExceptionHandler exceptionHandler,
                           ResponseTransformer<AuthResponse> responseTransformer) {
        this.serviceEndpoint = endpoints;
        this.sessionContext = sessionContext;
        this.exceptionHandler = exceptionHandler;
        this.responseTransformer = responseTransformer;
    }

    @Override
    public void handleSignIn(RouteDefinition process) throws Exception {
       process
               .marshal().json(JsonLibrary.Jackson)
               .to(MessageFormat.format("{0}/auth/login?bridgeEndpoint=true", serviceEndpoint.getAuthServiceUrl()))
               .unmarshal().json(JsonLibrary.Jackson)
               .process(exchange -> responseTransformer.transformToDataResponseMessage(exchange, SignInResponse.class))

               // Set client details in an in memory store
               .process(exchange -> {
                   var response = ((DataResponse) exchange.getMessage().getBody()).getData();
                   sessionContext.setClientContext((SignInResponse) response);
               })
               .onException(Exception.class)
               .onExceptionOccurred(exceptionHandler)
               .handled(true)
               .end();
    }


    @Override
    public void handleUpdateProfile(RouteDefinition process) throws Exception {
        process.setExchangePattern(ExchangePattern.InOut)
                .marshal().json(JsonLibrary.Jackson)
                .setHeader(Exchange.HTTP_METHOD, HttpMethods.PUT)
                .to(MessageFormat.format("{0}/client/update-profile?bridgeEndpoint=true", serviceEndpoint.getAuthServiceUrl()))
                .unmarshal().json(JsonLibrary.Jackson)
                .process(exchange -> responseTransformer.transformToDataResponseMessage(exchange, Client.class))
                .process(this::sendProfileUpdateNotification)
                .onException(Exception.class)
                .onExceptionOccurred(exceptionHandler)
                .handled(true)
                .end();
    }

    @Override
    public void handleSignUp(RouteDefinition process) throws Exception {
        process.setExchangePattern(ExchangePattern.InOut)
                .marshal().json(JsonLibrary.Jackson)
                .to(MessageFormat.format("{0}/auth/signup?bridgeEndpoint=true", serviceEndpoint.getAuthServiceUrl()))
                .unmarshal().json(JsonLibrary.Jackson)
                .process(exchange -> responseTransformer.transformToDataResponseMessage(exchange, SignInResponse.class))
                .process(this::sendRegistrationNotification)
                .onException(Exception.class)
                .onExceptionOccurred(exceptionHandler)
                .handled(true)
                .end();
    }

    /**
     * Creates the registration welcome email and sends to registered client user/platform
     * @param exchange The camel process context exchange object
     */
    private void sendRegistrationNotification (Exchange exchange) {
        var response = ((DataResponse<SignInResponse>) exchange.getMessage().getBody()).getData();
        ProducerTemplate template = exchange.getContext().createProducerTemplate();
        template.asyncSendBody("direct:send-notification",
                new NotificationRequest(
                        response.getClient().getEmail(),
                        response.getClient().getName(),
                        "Inventory App: Welcome On-Board !!",
                        "We're delighted to have you on board." +
                                " To proceed you'd have to select a subscription package and get going." +
                                " For additional support, please reach out to our support team on 123456789")

        );
    }



    /**
     * Creates the profile update notification and sends to registered client user/platform
     * @param exchange The camel process context exchange object
     */
    private void sendProfileUpdateNotification (Exchange exchange) {
        var response = ((DataResponse<Client>) exchange.getMessage().getBody()).getData();
        ProducerTemplate template = exchange.getContext().createProducerTemplate();
        template.asyncSendBody("direct:send-notification",
                new NotificationRequest(
                        response.getEmail(),
                        response.getName(),
                        "Inventory App: Profile Update",
                        "This mail is to inform you that your account has just been updated. If this was not authorized by you. Kindly contact the support team." +
                                "\n For additional support, please reach out to our support team on 123456789")

        );
    }

}
