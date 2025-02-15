package tn.esprit.entities;

public class RegularUser extends User {
    public RegularUser(String cin, String prenom, String nom, String tel, String mail, String password) {
        super(cin, prenom, nom, tel, mail, password, Role.REGULAR_USER);
    }

    public void accessServices() {
        System.out.println("Regular user accessing services...");
    }
}
