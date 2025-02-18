package models;

import java.sql.Date;

public class reservation_hebergement {
    private int id;
    private int id_hebergement;
    private Date date_debut;
    private Date date_fin;
    private int nb_perso;

    public reservation_hebergement(int id, int id_hebergement, Date date_debut, Date date_fin, int nb_perso) {
        this.id = id;
        this.id_hebergement = id_hebergement;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.nb_perso = nb_perso;
    }

    public reservation_hebergement(int id_hebergement, Date date_debut, Date date_fin, int nb_perso) {
        this.id_hebergement = id_hebergement;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.nb_perso = nb_perso;
    }

    public reservation_hebergement() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_hebergement() {
        return id_hebergement;
    }

    public void setId_hebergement(int id_hebergement) {
        this.id_hebergement = id_hebergement;
    }

    public Date getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(Date date_debut) {
        this.date_debut = date_debut;
    }

    public Date getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(Date date_fin) {
        this.date_fin = date_fin;
    }

    public int getNb_perso() {
        return nb_perso;
    }

    public void setNb_perso(int nb_perso) {
        this.nb_perso = nb_perso;
    }

    @Override
    public String toString() {
        return "ReservationHebergement{" +
                "id=" + id +
                ", id_hebergement=" + id_hebergement +
                ", date_debut='" + date_debut + '\'' +
                ", date_fin='" + date_fin + '\'' +
                ", nb_perso=" + nb_perso +
                '}';
    }
}
