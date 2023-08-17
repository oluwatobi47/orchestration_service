package uk.ac.bcu.invorchestrator.service;

import org.apache.camel.Exchange;

public interface ResponseTransformer<T> {
    <Type> void transformToDataResponseMessage(Exchange exchange, Class<Type> dataType);
}
