package fr.inti.cosmiconions.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Projet.
 */
@Entity
@Table(name = "projet")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Projet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @Lob
    @Column(name = "video")
    private byte[] video;

    @Column(name = "video_content_type")
    private String videoContentType;

    @Column(name = "objectif")
    private Float objectif;

    @Column(name = "solde_cours")
    private Float soldeCours;

    @Column(name = "nb_jours_restant")
    private Integer nbJoursRestant;

    @OneToMany(mappedBy = "projet")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Don> dons = new HashSet<>();

    @OneToMany(mappedBy = "projet")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Message> messages = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("projets")
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("projets")
    private Categorie categorie;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Projet description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public Projet photo(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public Projet photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public byte[] getVideo() {
        return video;
    }

    public Projet video(byte[] video) {
        this.video = video;
        return this;
    }

    public void setVideo(byte[] video) {
        this.video = video;
    }

    public String getVideoContentType() {
        return videoContentType;
    }

    public Projet videoContentType(String videoContentType) {
        this.videoContentType = videoContentType;
        return this;
    }

    public void setVideoContentType(String videoContentType) {
        this.videoContentType = videoContentType;
    }

    public Float getObjectif() {
        return objectif;
    }

    public Projet objectif(Float objectif) {
        this.objectif = objectif;
        return this;
    }

    public void setObjectif(Float objectif) {
        this.objectif = objectif;
    }

    public Float getSoldeCours() {
        return soldeCours;
    }

    public Projet soldeCours(Float soldeCours) {
        this.soldeCours = soldeCours;
        return this;
    }

    public void setSoldeCours(Float soldeCours) {
        this.soldeCours = soldeCours;
    }

    public Integer getNbJoursRestant() {
        return nbJoursRestant;
    }

    public Projet nbJoursRestant(Integer nbJoursRestant) {
        this.nbJoursRestant = nbJoursRestant;
        return this;
    }

    public void setNbJoursRestant(Integer nbJoursRestant) {
        this.nbJoursRestant = nbJoursRestant;
    }

    public Set<Don> getDons() {
        return dons;
    }

    public Projet dons(Set<Don> dons) {
        this.dons = dons;
        return this;
    }

    public Projet addDon(Don don) {
        this.dons.add(don);
        don.setProjet(this);
        return this;
    }

    public Projet removeDon(Don don) {
        this.dons.remove(don);
        don.setProjet(null);
        return this;
    }

    public void setDons(Set<Don> dons) {
        this.dons = dons;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public Projet messages(Set<Message> messages) {
        this.messages = messages;
        return this;
    }

    public Projet addMessage(Message message) {
        this.messages.add(message);
        message.setProjet(this);
        return this;
    }

    public Projet removeMessage(Message message) {
        this.messages.remove(message);
        message.setProjet(null);
        return this;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public User getUser() {
        return user;
    }

    public Projet user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public Projet categorie(Categorie categorie) {
        this.categorie = categorie;
        return this;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Projet)) {
            return false;
        }
        return id != null && id.equals(((Projet) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Projet{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", video='" + getVideo() + "'" +
            ", videoContentType='" + getVideoContentType() + "'" +
            ", objectif=" + getObjectif() +
            ", soldeCours=" + getSoldeCours() +
            ", nbJoursRestant=" + getNbJoursRestant() +
            "}";
    }
}
