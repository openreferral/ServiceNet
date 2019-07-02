package org.benetech.servicenet.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import java.util.Objects;

/**
 * A Eligibility.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "eligibility")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Eligibility extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @NotNull
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "eligibility", nullable = false, columnDefinition = "clob")
    private String eligibility = "";

    @OneToOne
    @JoinColumn(unique = true)
    private Service srvc;

    public Eligibility eligibility(String eligibility) {
        this.eligibility = eligibility;
        return this;
    }

    public Eligibility srvc(Service service) {
        this.srvc = service;
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
        Eligibility eligibilityObj = (Eligibility) o;
        if (eligibilityObj.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), eligibilityObj.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
