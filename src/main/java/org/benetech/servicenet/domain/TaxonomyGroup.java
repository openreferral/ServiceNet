package org.benetech.servicenet.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TaxonomyGroup.
 */
@Entity
@Table(name = "taxonomy_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TaxonomyGroup extends AbstractEntity implements Serializable {

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "taxonomy_group_taxonomies",
               joinColumns = @JoinColumn(name = "taxonomy_group_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "taxonomies_id", referencedColumnName = "id"))
    private Set<Taxonomy> taxonomies = new HashSet<>();

    public Set<Taxonomy> getTaxonomies() {
        return taxonomies;
    }

    public TaxonomyGroup taxonomies(Set<Taxonomy> taxonomies) {
        this.taxonomies = taxonomies;
        return this;
    }

    public TaxonomyGroup addTaxonomies(Taxonomy taxonomy) {
        this.taxonomies.add(taxonomy);
        return this;
    }

    public TaxonomyGroup removeTaxonomies(Taxonomy taxonomy) {
        this.taxonomies.remove(taxonomy);
        return this;
    }

    public void setTaxonomies(Set<Taxonomy> taxonomies) {
        this.taxonomies = taxonomies;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaxonomyGroup)) {
            return false;
        }
        return getId() != null && getId().equals(((TaxonomyGroup) o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TaxonomyGroup{" +
            "id=" + getId() +
            "}";
    }
}
