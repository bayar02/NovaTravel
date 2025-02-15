package tn.esprit.entities;

public class Agent extends User {
    public Agent(String cin, String prenom, String nom, String tel, String mail, String password) {
        super(cin, prenom, nom, tel, mail, password, Role.AGENT);
    }

    public void processRequests() {
        System.out.println("Agent " + getNom() + " is processing requests.");
    }
}
