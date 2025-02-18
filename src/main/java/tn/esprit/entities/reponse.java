package tn.esprit.entities;

import java.sql.Date;

public class reponse {
    private int id;
    private int id_reclamation;
    private String message;
    private Date date_reponse;

    // Default constructor
    public reponse() {
    }

    // Constructor with all attributes
    public reponse(int id, int id_reclamation, String message, Date date_reponse) {
        this.id = id;
        this.id_reclamation = id_reclamation;
        this.message = message;
        this.date_reponse = date_reponse;
    }

    // Constructor without id (useful for creating a response before saving it to a database)
    public reponse(int id_reclamation, String message, Date date_reponse) {
        this.id_reclamation = id_reclamation;
        this.message = message;
        this.date_reponse = date_reponse;
    }



    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_reclamation() {
        return id_reclamation;
    }

    public void setId_reclamation(int id_reclamation) {
        this.id_reclamation = id_reclamation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate_reponse() {
        return date_reponse;
    }

    public void setDate_reponse(Date date_reponse) {
        this.date_reponse = date_reponse;
    }

    // toString method to represent the object as a string
    @Override
    public String toString() {
        return "Reponse {" +
                "id=" + id +
                ", id_reclamation=" + id_reclamation +
                ", message='" + message + '\'' +
                ", date_reponse='" + date_reponse + '\'' +
                '}';
    }
}


