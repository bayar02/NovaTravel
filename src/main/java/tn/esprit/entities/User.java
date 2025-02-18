package tn.esprit.entities;

import java.util.Objects;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private int number;
    private String mail;
    private String password;



    public User() {
    }

    public User(String nom, String prenom, int number, String mail, String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.number = number;
        this.mail = mail;
        this.password = password;
    }
    public User(int id ,String nom) {
        this.id = id ;
        this.nom = nom;
    }
    public User(int id ,String nom,String prenom) {
        this.id = id ;
        this.nom = nom;
        this.prenom = prenom ;
    }
    public User(String nom) {
        this.nom = nom;
    }


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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public User(int id, String nom, String prenom, int number, String mail, String password) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.number = number;
        this.mail = mail;
        this.password = password;
    }



    @Override
    public String toString() {
        return "User{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", number=" + number +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +

                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }



}