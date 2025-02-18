package models;

public class hebergement {
    private int id;
    private String type, nom, adresse, description, photo;
    private float prix_nuit;

    public hebergement(int id, String type, String nom, String adresse, String description, float prix_nuit) {
        this.id = id;
        this.type = type;
        this.nom = nom;
        this.adresse = adresse;
        this.description = description;
        this.prix_nuit = prix_nuit;

    }



    public hebergement(String nom, String type, String adresse, String description, float prix_nuit) {

        this.type = type;
        this.nom = nom;
        this.adresse = adresse;
        this.description = description;
        this.prix_nuit = prix_nuit;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrix_nuit() {
        return prix_nuit;
    }

    public void setPrix_nuit(float prix_nuit) {
        this.prix_nuit = prix_nuit;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Hebergement{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", description='" + description + '\'' +
                ", prix_nuit=" + prix_nuit +
                ", photo='" + photo + '\'' +
                '}';
    }
}