package Entities;

import java.time.LocalDate;
import java.util.Date;

public class Reclamation {
    private int id;
    private int idUser;
    private Date dateReclamation;
    private String type;
    private String message;

    // Constructors
    public Reclamation() {}

    public Reclamation(int id, int idUser, Date dateReclamation, String type, String message) {
        this.id = id;
        this.idUser = idUser;
        this.dateReclamation = dateReclamation;
        this.type = type;
        this.message = message;
    }

    public Reclamation( int idUser, Date dateReclamation, String type, String message) {
        this.idUser = idUser;
        this.dateReclamation = dateReclamation;
        this.type = type;
        this.message = message;
    }
    public Reclamation(int idReclamation, String message) {
        this.id = idReclamation;
        this.message = message;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Date getDateReclamation() {
        return dateReclamation;
    }

    public void setDateReclamation(Date dateReclamation) {
        this.dateReclamation = dateReclamation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // To String Method
    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", dateReclamation=" + dateReclamation +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
