package tn.esprit.monji.model;

import java.util.Date;

public class Vol {
    private int id;
    private String compagnie;
    private String aeroportDepart;
    private String aeroportArrivee;
    private String destination;
    private Date dateDepart;
    private Date dateArrivee;
    private double prix;

    // Constructors
    public Vol() {}

    public Vol(int id, String compagnie, String aeroportDepart, String aeroportArrivee, 
               String destination, Date dateDepart, Date dateArrivee, double prix) {
        this.id = id;
        this.compagnie = compagnie;
        this.aeroportDepart = aeroportDepart;
        this.aeroportArrivee = aeroportArrivee;
        this.destination = destination;
        this.dateDepart = dateDepart;
        this.dateArrivee = dateArrivee;
        this.prix = prix;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompagnie() {
        return compagnie;
    }

    public void setCompagnie(String compagnie) {
        this.compagnie = compagnie;
    }

    public String getAeroportDepart() {
        return aeroportDepart;
    }

    public void setAeroportDepart(String aeroportDepart) {
        this.aeroportDepart = aeroportDepart;
    }

    public String getAeroportArrivee() {
        return aeroportArrivee;
    }

    public void setAeroportArrivee(String aeroportArrivee) {
        this.aeroportArrivee = aeroportArrivee;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Date getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(Date dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Vol{" +
                "id=" + id +
                ", compagnie='" + compagnie + '\'' +
                ", aeroportDepart='" + aeroportDepart + '\'' +
                ", aeroportArrivee='" + aeroportArrivee + '\'' +
                ", destination='" + destination + '\'' +
                ", dateDepart=" + dateDepart +
                ", dateArrivee=" + dateArrivee +
                ", prix=" + prix +
                '}';
    }
} 