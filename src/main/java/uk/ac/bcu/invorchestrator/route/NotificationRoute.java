package uk.ac.bcu.invorchestrator.route;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpMethods;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;
import uk.ac.bcu.invorchestrator.config.ServiceEndpoints;

import java.text.MessageFormat;

@Component
public class NotificationRoute extends RouteBuilder {

    private final ServiceEndpoints serviceEndpoint;

    public NotificationRoute(ServiceEndpoints serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
    }

    /**
     * Registers camel endpoint to handle all email notification requests
     * @throws Exception Unhandled exception from spring or camel context outside implementation scope
     */
    @Override
    public void configure() throws Exception {
        from("direct:send-notification")
                .setExchangePattern(ExchangePattern.InOnly)
                .log("Sending Notification: ${body}")
                .marshal().json(JsonLibrary.Jackson)

                // Forward notification request from external process to notification service
                .to(MessageFormat.format("{0}/notification/send?bridgeEndpoint=true",
                        serviceEndpoint.getNotificationServiceUrl()))
                .setHeader(Exchange.HTTP_METHOD, HttpMethods.POST)
                .onException(Exception.class)
                .log(">>> ${exception}")
                .handled(true);
    }
}
