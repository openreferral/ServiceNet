package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.FetchType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DocumentUpload.
 */
@Entity
@Table(name = "document_upload")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DocumentUpload extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "date_uploaded", nullable = false)
    private ZonedDateTime dateUploaded;

    @NotNull
    @Column(name = "original_document_id", nullable = false, unique = true)
    private String originalDocumentId;

    @Column(name = "parsed_document_id", unique = true)
    private String parsedDocumentId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @NotNull
    @JsonIgnoreProperties("")
    private UserProfile uploader;

    @Transient
    private String filename;

    @Transient
    private String delimiter;

    public DocumentUpload() {
    }

    public DocumentUpload(@NotNull String originalDocumentId, String parsedDocumentId) {
        this.originalDocumentId = originalDocumentId;
        this.parsedDocumentId = parsedDocumentId;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public ZonedDateTime getDateUploaded() {
        return dateUploaded;
    }

    public DocumentUpload dateUploaded(ZonedDateTime dateUploaded) {
        this.dateUploaded = dateUploaded;
        return this;
    }

    public void setDateUploaded(ZonedDateTime dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public String getOriginalDocumentId() {
        return originalDocumentId;
    }

    public DocumentUpload originalDocumentId(String originalDocumentId) {
        this.originalDocumentId = originalDocumentId;
        return this;
    }

    public void setOriginalDocumentId(String originalDocumentId) {
        this.originalDocumentId = originalDocumentId;
    }

    public String getParsedDocumentId() {
        return parsedDocumentId;
    }

    public DocumentUpload parsedDocumentId(String parsedDocumentId) {
        this.parsedDocumentId = parsedDocumentId;
        return this;
    }

    public void setParsedDocumentId(String parsedDocumentId) {
        this.parsedDocumentId = parsedDocumentId;
    }

    public UserProfile getUploader() {
        return uploader;
    }

    public DocumentUpload uploader(UserProfile userProfile) {
        this.uploader = userProfile;
        return this;
    }

    public void setUploader(UserProfile userProfile) {
        this.uploader = userProfile;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
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
            ", originalDocumentId='" + getOriginalDocumentId() + "'" +
            ", parsedDocumentId='" + getParsedDocumentId() + "'" +
            "}";
    }
}
