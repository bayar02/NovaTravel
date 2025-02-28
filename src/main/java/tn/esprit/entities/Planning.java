package tn.esprit.entities;

import java.util.ArrayList;
import java.util.List;

public class Planning {


    private int id;
    private String dateCreation;
    private String nom;
    private List<Event> events = new ArrayList<>();



    public Planning() {
    }

    public Planning(int id, String dateCreation,String nom , List<Event> events) {


        this.id = id;
        this.dateCreation = dateCreation;
        this.nom = nom;
        this.events = events != null ? events : new ArrayList<>();
    }
    public Planning( String dateCreation,String nom , List<Event> events) {



        this.dateCreation = dateCreation;
        this.nom = nom;
        this.events = events != null ? events : new ArrayList<>();
    }

    public Planning(String dateCreation) {
        this.dateCreation = dateCreation;
        this.events = new ArrayList<>();
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "Planning {" +
                "id=" + id +
                ", dateCreation='" + dateCreation + '\'' +
                ", nom='" + nom + '\'' +
                ", events=" + events +
                '}';
    }

}
