package uk.ac.bcu.invorchestrator.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultMessage;
import org.springframework.stereotype.Service;
import uk.ac.bcu.invorchestrator.dto.DataResponse;
import uk.ac.bcu.invorchestrator.dto.auth.AuthResponse;
import uk.ac.bcu.invorchestrator.service.ResponseTransformer;

@Service
public class AuthResponseTransformer implements ResponseTransformer<AuthResponse> {

    private final ObjectMapper mapper;

    public AuthResponseTransformer(ObjectMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public <Type> void transformToDataResponseMessage(Exchange exchange, Class<Type> dataType) {
        var body = exchange.getMessage().getBody();
        var rawApiResponse = mapper.convertValue(body, AuthResponse.class);
        Type responseData = null;
        String apiResponseMessage = "";
        if (rawApiResponse != null) {
            responseData = mapper.convertValue(rawApiResponse.getData(), dataType);
            apiResponseMessage = rawApiResponse.getMessage();
        }
        var message = new DefaultMessage(exchange);
        var response = new DataResponse<Type>(responseData, true, apiResponseMessage);
        message.setBody(response);
        exchange.setMessage(message);
    }

}
