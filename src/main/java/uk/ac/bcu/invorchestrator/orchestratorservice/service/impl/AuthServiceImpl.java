package uk.ac.bcu.invorchestrator.orchestratorservice.service.impl;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Expression;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.support.DefaultMessage;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Service;
import uk.ac.bcu.invorchestrator.orchestratorservice.config.ServiceEndpoints;
import uk.ac.bcu.invorchestrator.orchestratorservice.dto.DataResponse;
import uk.ac.bcu.invorchestrator.orchestratorservice.dto.auth.SignInRequest;
import uk.ac.bcu.invorchestrator.orchestratorservice.dto.auth.SignInResponse;
import uk.ac.bcu.invorchestrator.orchestratorservice.service.AuthService;
import uk.ac.bcu.invorchestrator.orchestratorservice.service.SessionContext;

import java.text.MessageFormat;

@Service
public class AuthServiceImpl implements AuthService {

    private final ServiceEndpoints endpoints;

    private final SessionContext sessionContext;

    public AuthServiceImpl(ServiceEndpoints endpoints, SessionContext sessionContext) {
        this.endpoints = endpoints;
        this.sessionContext = sessionContext;
    }


    @Override
    public void handleSignIn(RouteDefinition process) throws Exception {
       process.setExchangePattern(ExchangePattern.InOut)
//               .inputType(SignInRequest.class)
               .marshal().json(JsonLibrary.Jackson)
               .log(MessageFormat.format("Logging some value {0}", this.endpoints.getAuth()))
               .toD(MessageFormat.format("{0}/login?bridgeEndpoint=true", endpoints.getAuth()))
               .transform(new Expression() {
                   @Override
                   public <T> T evaluate(Exchange exchange, Class<T> type) {
                       var response = exchange.getIn().getBody(SignInResponse.class);
                       sessionContext.setClientContext(response);
                       return (T) new DataResponse<SignInResponse>(response);
                   }
               })
               // TODO: Write function to handle this generically for all api responses
               .onException(Exception.class)
               .log("An exception occurred: ${exception.message}")
               .onExceptionOccurred(exchange -> {
                   var exception = exchange.getException(Exception.class);
                   var response = new DataResponse<String>(null, false, exception != null ? exception.getMessage() : "An unknown error occurred");
                   var message = new DefaultMessage(exchange);
                   message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
                   if(exception instanceof HttpOperationFailedException) {
                       var parser = new GsonJsonParser();
                       var responseBody = parser.parseMap(((HttpOperationFailedException) exception).getResponseBody());
                       response = new DataResponse<String>(null, false, (String) responseBody.get("message"));
                       message.setHeader(Exchange.HTTP_RESPONSE_CODE, ((HttpOperationFailedException) exception).getStatusCode());
                   }
                   message.setBody(response);
                   exchange.setMessage(message);
               })
               .handled(true)
               .end();
    }

    @Override
    public void signIn(Exchange exchange) throws Exception {
        var request = exchange.getIn().getBody(SignInRequest.class);
    }

    @Override
    public void getClientInformation(Exchange exchange) throws Exception {

    }

    @Override
    public void signUp(Exchange exchange) throws Exception {

    }

    @Override
    public void signOut(Exchange exchange) throws Exception {

    }
}
