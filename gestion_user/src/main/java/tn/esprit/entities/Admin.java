package tn.esprit.entities;

public class Admin extends User {
    public Admin(String cin, String prenom, String nom, String tel, String mail, String password) {
        super(cin, prenom, nom, tel, mail, password, Role.ADMIN);
    }

    public void manageUsers() {
        System.out.println("Admin managing users...");
    }
}
