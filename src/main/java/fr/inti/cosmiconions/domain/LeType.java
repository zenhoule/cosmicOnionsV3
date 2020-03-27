package fr.inti.cosmiconions.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A LeType.
 */
@Entity
@Table(name = "le_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LeType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @OneToMany(mappedBy = "leType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Projet> projets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public LeType nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<Projet> getProjets() {
        return projets;
    }

    public LeType projets(Set<Projet> projets) {
        this.projets = projets;
        return this;
    }

    public LeType addProjet(Projet projet) {
        this.projets.add(projet);
        projet.setLeType(this);
        return this;
    }

    public LeType removeProjet(Projet projet) {
        this.projets.remove(projet);
        projet.setLeType(null);
        return this;
    }

    public void setProjets(Set<Projet> projets) {
        this.projets = projets;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeType)) {
            return false;
        }
        return id != null && id.equals(((LeType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "LeType{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
