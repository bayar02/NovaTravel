package tn.esprit.entities;

import java.util.ArrayList;
import java.util.List;



public class Event {

    private int id;
    private String nom;
    private String description;
    private String lieu;
    private String dateEvent;
    private int duree; // in hours
    private float prix;
    //private List<Planning> plannings;


    /*public Event() {
        this.plannings = new ArrayList<>();
    }*/
    public Event() {
    }

    public Event(int eventId, String nom) {
        this.id = eventId;
        this.nom = nom;
    }

    public Event( String nom, String description, String lieu, String dateEvent, int duree, float prix) {

        this.nom = nom;
        this.description = description;
        this.lieu = lieu;
        this.dateEvent = dateEvent;
        this.duree = duree;
        this.prix = prix;

    }

    public Event(int id, String nom, String description, String lieu, String dateEvent, int duree, float prix) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.lieu = lieu;
        this.dateEvent = dateEvent;
        this.duree = duree;
        this.prix = prix;

    }

    /*public Event(int id, String nom, String description, String lieu, String dateEvent, int duree, float prix, List<Planning> plannings) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.lieu = lieu;
        this.dateEvent = dateEvent;
        this.duree = duree;
        this.prix = prix;
        this.plannings = plannings != null ? plannings : new ArrayList<>();
    }*/





    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(String dateEvent) {
        this.dateEvent = dateEvent;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    /*public List<Planning> getPlannings() {
        return plannings;
    }

    public void setPlannings(List<Planning> plannings) {
        this.plannings = plannings;
    }*/

    @Override
    public String toString() {
        return "Event {" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", lieu='" + lieu + '\'' +
                ", dateEvent='" + dateEvent + '\'' +
                ", duree=" + duree +
                ", prix=" + prix +
                '}';
    }


}
