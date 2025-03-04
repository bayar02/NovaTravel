package Entities;

import java.time.LocalDate;
import java.util.Date;

public class Reponse {
    private int id;
    private int idReclamation;
    private String message;
    private Date dateReponse;

    // Constructors
    public Reponse() {}

    public Reponse( int idReclamation, String message, Date dateReponse) {
        this.idReclamation = idReclamation;
        this.message = message;
        this.dateReponse = dateReponse;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdReclamation() {
        return idReclamation;
    }

    public void setIdReclamation(int idReclamation) {
        this.idReclamation = idReclamation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateReponse() {
        return dateReponse;
    }

    public void setDateReponse(Date dateReponse) {
        this.dateReponse = dateReponse;
    }

    // To String Method
    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id +
                ", idReclamation=" + idReclamation +
                ", message='" + message + '\'' +
                ", dateReponse=" + dateReponse +
                '}';
    }
}
