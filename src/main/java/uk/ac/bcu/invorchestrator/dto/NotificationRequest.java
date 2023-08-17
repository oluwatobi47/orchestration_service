package uk.ac.bcu.invorchestrator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Notification service request model for sending emails
 */
public class NotificationRequest {

    @JsonProperty("client_email")
    private String clientEmail;

    @JsonProperty("client_name")
    private String clientName;

    @JsonProperty("msg_subject")
    private String msgSubject;

    @JsonProperty("msg_body")
    private String msgBody;

    public NotificationRequest(String clientEmail, String clientName, String msgSubject, String msgBody) {
        this.clientEmail = clientEmail;
        this.clientName = clientName;
        this.msgSubject = msgSubject;
        this.msgBody = msgBody;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String value) {
        this.clientEmail = value;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String value) {
        this.clientName = value;
    }

    public String getMsgSubject() {
        return msgSubject;
    }
    public void setMsgSubject(String value) {
        this.msgSubject = value; }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String value) {
        this.msgBody = value;
    }
}
