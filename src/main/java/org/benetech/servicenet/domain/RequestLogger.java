package org.benetech.servicenet.domain;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


import java.io.Serializable;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * A RequestLogger.
 */
@Entity
@Table(name = "request_logger")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RequestLogger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "request_uri")
    private String requestUri;

    @Column(name = "remote_addr")
    private String remoteAddr;

    @Column(name = "request_method")
    private String requestMethod;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "request_parameters", columnDefinition = "clob")
    private String requestParameters;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "request_body", columnDefinition = "clob")
    private String requestBody;

    @Column(name = "response_status")
    private String responseStatus;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "response_body", columnDefinition = "clob")
    private String responseBody;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public RequestLogger requestUri(String requestUri) {
        this.requestUri = requestUri;
        return this;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public RequestLogger remoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
        return this;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRequestParameters() {
        return requestParameters;
    }

    public RequestLogger requestParameters(String requestParameters) {
        this.requestParameters = requestParameters;
        return this;
    }

    public void setRequestParameters(String requestParameters) {
        this.requestParameters = requestParameters;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public RequestLogger requestBody(String requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public RequestLogger responseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
        return this;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public RequestLogger requestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
        return this;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public RequestLogger responseBody(String responseBody) {
        this.responseBody = responseBody;
        return this;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequestLogger)) {
            return false;
        }
        return id != null && id.equals(((RequestLogger) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RequestLogger{" +
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
