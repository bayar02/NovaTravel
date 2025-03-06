package tn.esprit.monji.model;

public class ReservationVol {
    private int id;
    private int idUser;
    private int idVol;
    private String classe;
    private int nbBillets;

    // Constructors
    public ReservationVol() {}

    public ReservationVol(int id, int idUser, int idVol, String classe, int nbBillets) {
        this.id = id;
        this.idUser = idUser;
        this.idVol = idVol;
        this.classe = classe;
        this.nbBillets = nbBillets;
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

    public int getIdVol() {
        return idVol;
    }

    public void setIdVol(int idVol) {
        this.idVol = idVol;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public int getNbBillets() {
        return nbBillets;
    }

    public void setNbBillets(int nbBillets) {
        this.nbBillets = nbBillets;
    }

    @Override
    public String toString() {
        return "ReservationVol{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", idVol=" + idVol +
                ", classe='" + classe + '\'' +
                ", nbBillets=" + nbBillets +
                '}';
    }
} 