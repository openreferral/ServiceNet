package org.benetech.servicenet.service.dto;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link org.benetech.servicenet.domain.RequestLogger} entity.
 */
public class RequestLoggerDTO implements Serializable {

    private UUID id;

    private String requestUri;

    private String remoteAddr;

    private String requestParameters;

    private String requestBody;

    private String responseStatus;

    private String requestMethod;

    private String responseBody;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(String requestParameters) {
        this.requestParameters = requestParameters;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RequestLoggerDTO requestLoggerDTO = (RequestLoggerDTO) o;
        if (requestLoggerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), requestLoggerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RequestLoggerDTO{" +
            "id=" + getId() +
            ", requestUri='" + getRequestUri() + "'" +
            ", remoteAddr='" + getRemoteAddr() + "'" +
            ", requestParameters='" + getRequestParameters() + "'" +
            ", requestBody='" + getRequestBody() + "'" +
            ", responseStatus='" + getResponseStatus() + "'" +
            ", requestMethod='" + getRequestMethod() + "'" +
            ", responseBody='" + getResponseBody() + "'" +
            "}";
    }
}
