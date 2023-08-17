package uk.ac.bcu.invorchestrator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Bean file for service endpoints defined in the environment properties
 */
@Component
@ConfigurationProperties("inv.service")
public class ServiceEndpoints {

    private String auth;
    private String warehouse;
    private String order;
    private String notification;
    private String subscription;

    public String getAuthServiceUrl() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getWarehouseServiceUrl() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getOrderServiceUrl() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getNotificationServiceUrl() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getSubscriptionServiceUrl() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

}
