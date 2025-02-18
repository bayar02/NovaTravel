package tn.esprit.models;

public class Pack {
    private String nomPack;
    private String description;
    private Double prix;
    private String duree;
    private String avantages;
    private String statut;

    public Pack(String nomPack, String description, Double prix, String duree, String avantages, String statut) {
        this.nomPack = nomPack;
        this.description = description;
        this.prix = prix;
        this.duree = duree;
        this.avantages = avantages;
        this.statut = statut;
    }

    // Getters and Setters
    public String getNomPack() { return nomPack; }
    public void setNomPack(String nomPack) { this.nomPack = nomPack; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }
    
    public String getDuree() { return duree; }
    public void setDuree(String duree) { this.duree = duree; }
    
    public String getAvantages() { return avantages; }
    public void setAvantages(String avantages) { this.avantages = avantages; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
} 