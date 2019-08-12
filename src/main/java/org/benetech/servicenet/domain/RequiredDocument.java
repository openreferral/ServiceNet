package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A RequiredDocument.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "required_document", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"external_db_id", "provider_name"})
})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RequiredDocument extends AbstractEntity implements Serializable, DeepComparable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "document", nullable = false, columnDefinition = "clob")
    private String document = "";

    @ManyToOne
    @JsonIgnoreProperties("docs")
    private Service srvc;

    @Column(name = "external_db_id")
    @Size(max = 255, message = "Field value is too long.")
    private String externalDbId;

    @Column(name = "provider_name")
    @Size(max = 255, message = "Field value is too long.")
    private String providerName;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public RequiredDocument document(String document) {
        this.document = document;
        return this;
    }

    public RequiredDocument srvc(Service service) {
        this.srvc = service;
        return this;
    }

    public RequiredDocument externalDbId(String externalDbId) {
        this.externalDbId = externalDbId;
        return this;
    }

    public RequiredDocument providerName(String providerName) {
        this.providerName = providerName;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequiredDocument requiredDocument = (RequiredDocument) o;
        if (requiredDocument.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), requiredDocument.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean deepEquals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequiredDocument rd = (RequiredDocument) o;
        return Objects.equals(document, rd.document) &&
            Objects.equals(externalDbId, rd.externalDbId) &&
            Objects.equals(providerName, rd.providerName);
    }
}
