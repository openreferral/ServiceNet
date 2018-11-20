package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DocumentUpload.
 */
@Entity
@Table(name = "document_upload")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DocumentUpload implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @NotNull
    @Column(name = "date_uploaded", nullable = false)
    private ZonedDateTime dateUploaded;

    @NotNull
    @Column(name = "document_id", nullable = false, unique = true)
    private String documentId;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User uploader;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ZonedDateTime getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(ZonedDateTime dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public DocumentUpload dateUploaded(ZonedDateTime dateUploaded) {
        this.dateUploaded = dateUploaded;
        return this;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public DocumentUpload documentId(String documentId) {
        this.documentId = documentId;
        return this;
    }

    public User getUploader() {
        return uploader;
    }

    public void setUploader(User user) {
        this.uploader = user;
    }

    public DocumentUpload uploader(User user) {
        this.uploader = user;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DocumentUpload documentUpload = (DocumentUpload) o;
        if (documentUpload.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), documentUpload.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DocumentUpload{" +
            "id=" + getId() +
            ", dateUploaded='" + getDateUploaded() + "'" +
            ", documentId='" + getDocumentId() + "'" +
            "}";
    }
}
