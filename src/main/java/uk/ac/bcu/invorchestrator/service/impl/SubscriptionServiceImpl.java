package uk.ac.bcu.invorchestrator.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.component.http.HttpMethods;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.support.DefaultMessage;
import org.springframework.stereotype.Service;
import uk.ac.bcu.invorchestrator.config.ServiceEndpoints;
import uk.ac.bcu.invorchestrator.dto.DataResponse;
import uk.ac.bcu.invorchestrator.dto.subscription.SubscriptionPlanDTO;
import uk.ac.bcu.invorchestrator.service.SubscriptionService;

import java.util.HashMap;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final ServiceEndpoints serviceEndpoint;
    private final ExceptionHandler exceptionHandler;
    private final ObjectMapper mapper;

    public SubscriptionServiceImpl(ServiceEndpoints serviceEndpoint, ExceptionHandler exceptionHandler, ObjectMapper mapper) {
        this.serviceEndpoint = serviceEndpoint;
        this.exceptionHandler = exceptionHandler;
        this.mapper = mapper;
    }


    private void buildClientIdPayload(Exchange exchange) {
        var clientId = exchange.getIn().getHeader("clientId");
        var body = new HashMap<String, Object>();
        body.put("client_id", clientId);
        var message = new DefaultMessage(exchange);
        message.setBody(body);
        exchange.setIn(message);
    }

    /**
     * Handles API transformation to end user client system
     */
    private <T> void transformToDataResponse(Exchange exchange, Class<T> type) {
        var body = exchange.getMessage().getBody();
        var rawApiResponse = mapper.convertValue(body, type);
        var message = new DefaultMessage(exchange);
        var response = new DataResponse<T>(rawApiResponse, true);
        message.setBody(response);
        exchange.setMessage(message);
    }

    /**
     * Handles API transformation to end user client system
     */
    private <T> void transformToDataResponse(Exchange exchange, TypeReference<T> type) {
        var body = exchange.getMessage().getBody();
        var rawApiResponse = mapper.convertValue(body, type);
        var message = new DefaultMessage(exchange);
        var response = new DataResponse<T>(rawApiResponse, true);
        message.setBody(response);
        exchange.setMessage(message);
    }

    @Override
    public void getServicePlans(RouteDefinition routeDefinition) {
        routeDefinition
                .marshal().json(JsonLibrary.Jackson) // Handles serialisation
                .toD(String.format("%s/api/get_plan?bridgeEndpoint=true", serviceEndpoint.getSubscriptionServiceUrl()))
                .unmarshal().json(JsonLibrary.Jackson) // Handles de-serialisation from external api response
                .process(exchange -> this.transformToDataResponse(exchange, new TypeReference<List<SubscriptionPlanDTO>>() {}))
                .onException(Exception.class)
                .onExceptionOccurred(exceptionHandler)
                .handled(true)
                .end();
    }

    @Override
    public void handleSubscribeToPlan(RouteDefinition routeDefinition) {
        routeDefinition
                .marshal().json(JsonLibrary.Jackson) // Handles serialisation
                .setHeader(Exchange.HTTP_METHOD, HttpMethods.POST)
                .toD(String.format("%s/api/subscribe?bridgeEndpoint=true", serviceEndpoint.getSubscriptionServiceUrl()))
                .unmarshal().json(JsonLibrary.Jackson) // Handles de-serialisation from external api response
                .process(exchange -> this.transformToDataResponse(exchange, Object.class))
                .onException(Exception.class)
                .onExceptionOccurred(exceptionHandler)
                .handled(true)
                .end();
    }

    @Override
    public void handleSubscriptionPayment(RouteDefinition routeDefinition) {
        routeDefinition
                .marshal().json(JsonLibrary.Jackson) // Handles serialisation
                .setHeader(Exchange.HTTP_METHOD, HttpMethods.POST)
                .toD(String.format("%s/api/make_payment?bridgeEndpoint=true", serviceEndpoint.getSubscriptionServiceUrl()))
                .unmarshal().json(JsonLibrary.Jackson) // Handles de-serialisation from external api response
                .process(exchange -> {
                    var message = new DefaultMessage(exchange);
                    var response = new DataResponse(null, true,  "Payment successful!");
                    message.setBody(response);
                    exchange.setMessage(message);
                })
                .onException(Exception.class)
                .onExceptionOccurred(exceptionHandler)
                .handled(true)
                .end();
    }

    @Override
    public void handleGetClientPayment(RouteDefinition routeDefinition) {
        routeDefinition
                // Transform request to external api payload
                .process(this::buildClientIdPayload)
                .marshal().json(JsonLibrary.Jackson) // Handles serialisation
                .setHeader(Exchange.HTTP_METHOD, HttpMethods.POST)
                .toD(String.format("%s/api/get_payments?bridgeEndpoint=true", serviceEndpoint.getSubscriptionServiceUrl()))
                .unmarshal().json(JsonLibrary.Jackson) // Handles de-serialisation from external api response
                .process(exchange -> this.transformToDataResponse(exchange, List.class))
                .onException(Exception.class)
                .onExceptionOccurred(exceptionHandler)
                .handled(true)
                .end();
    }

    @Override
    public void handleGetClientSubscription(RouteDefinition routeDefinition) {
        routeDefinition
                // Transform request to external api payload
                .process(this::buildClientIdPayload)
                .marshal().json(JsonLibrary.Jackson) // Handles serialisation
                .setHeader(Exchange.HTTP_METHOD, HttpMethods.POST)
                .to("log:DEBUG?showBody=true&showHeaders=true")
                .toD(String.format("%s/api/currentSubPlan?bridgeEndpoint=true", serviceEndpoint.getSubscriptionServiceUrl()))
                .unmarshal().json(JsonLibrary.Jackson) // Handles de-serialisation from external api response
                .process(exchange -> this.transformToDataResponse(exchange, Object.class))
                .onException(Exception.class)
                .onExceptionOccurred(exceptionHandler)
                .handled(true)
                .end();

    }
}
