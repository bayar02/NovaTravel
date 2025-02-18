package tn.esprit.entities;

public abstract class User {
    private int id_user;
    private String cin;
    private String prenom;
    private String nom;
    private String tel;
    private String mail;
    private String password;

    // Enum for user roles
    public enum Role {
        ADMIN, AGENT, REGULAR_USER
    }

    private Role role;

    public User(String cin, String prenom, String nom, String tel, String mail, String password, Role role) {
        this.cin = cin;
        this.prenom = prenom;
        this.nom = nom;
        this.tel = tel;
        this.mail = mail;
        this.password = password;
        this.role = role;
    }

    public int getId_user() { return id_user; }
    public void setId_user(int id_user) { this.id_user = id_user; }
    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    @Override
    public String toString() {
        return "User{" + "id_user=" + id_user + ", cin=" + cin + ", prenom=" + prenom + ", nom=" + nom +
                ", tel=" + tel + ", mail=" + mail + ", role=" + role + ", password=" + password + '}';
    }
}
