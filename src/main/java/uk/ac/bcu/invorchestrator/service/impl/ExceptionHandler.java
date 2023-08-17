package uk.ac.bcu.invorchestrator.service.impl;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.camel.support.DefaultMessage;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uk.ac.bcu.invorchestrator.dto.DataResponse;
import uk.ac.bcu.invorchestrator.util.ProcessValidationException;

import javax.xml.ws.soap.SOAPFaultException;

@Component
public class ExceptionHandler implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        var exception = exchange.getException(Exception.class);
        var response = new DataResponse<String>(null, false, exception != null && StringUtils.hasText(exception.getMessage()) ? exception.getMessage() : "An internal server error occurred. Please contact administrator for support");
        var message = new DefaultMessage(exchange);
        message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
        if (exception != null) {
            exception.printStackTrace();
            if(exception instanceof HttpOperationFailedException) {
                // Custom handling for responses from sub and payment service
                var isSubServiceRequest = exchange.getFromEndpoint().getEndpointUri().startsWith("servlet:/account");
                if (!isSubServiceRequest) {
                    var parser = new JSONParser(((HttpOperationFailedException) exception).getResponseBody());
                    if(!((HttpOperationFailedException) exception).getResponseBody().isEmpty()) {
                        var responseBody = parser.parseObject();
                        response = new DataResponse<String>(null, false, (String) responseBody.get("message"));
                    } else {
                        response.setMessage("An api error occurred. Please contact administrator for support");
                    }
                }
                message.setHeader(Exchange.HTTP_RESPONSE_CODE, ((HttpOperationFailedException) exception).getStatusCode());
            } else if (exception instanceof ProcessValidationException) {
                response = new DataResponse<String>(null, false, ((ProcessValidationException) exception).getMessage());
                message.setHeader(Exchange.HTTP_RESPONSE_CODE, ((ProcessValidationException) exception).getStatusCode().value());
            }  else if (exception instanceof SOAPFaultException) {
                response = new DataResponse<String>(null, false, ((SOAPFaultException) exception).getMessage());
                message.setHeader(Exchange.HTTP_RESPONSE_CODE, ((SOAPFaultException) exception).getFault().getFaultCode());
            }

        }
        message.setBody(response);
        exchange.setMessage(message);
    }
}
