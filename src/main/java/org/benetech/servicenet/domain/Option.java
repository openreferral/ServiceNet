package org.benetech.servicenet.domain;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

import org.benetech.servicenet.domain.enumeration.OptionType;
import org.hibernate.annotations.Type;

/**
 * A Option.
 */
@Entity
@Table(name = "option")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Option extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Enumerated(EnumType.STRING)
    @Lob
    @Type(type = "org.hibernate.type.EnumType")
    @Column(name = "jhi_type", columnDefinition = "clob")
    private OptionType type;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "jhi_value", nullable = false, columnDefinition = "clob")
    private String value;

    public OptionType getType() {
        return type;
    }

    public Option type(OptionType type) {
        this.type = type;
        return this;
    }

    public void setType(OptionType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public Option value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
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
        Option option = (Option) o;
        if (option.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), option.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Option{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", value=" + getValue() +
            "}";
    }
}
