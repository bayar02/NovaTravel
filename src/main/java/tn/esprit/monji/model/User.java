package tn.esprit.monji.model;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private String cin;
    private String mail;
    private String tel;
    private String role;

    public User(int id, String nom, String prenom, String cin, String mail, String tel, String role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        this.mail = mail;
        this.tel = tel;
        this.role = role;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getCin() { return cin; }
    public String getMail() { return mail; }
    public String getTel() { return tel; }
    public String getRole() { return role; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setCin(String cin) { this.cin = cin; }
    public void setMail(String mail) { this.mail = mail; }
    public void setTel(String tel) { this.tel = tel; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return String.format("%d - %s %s", id, nom, prenom);
    }
} 