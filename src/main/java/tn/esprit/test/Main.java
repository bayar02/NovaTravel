package tn.esprit.test;

import tn.esprit.entities.Event;
import tn.esprit.entities.Planning;
import tn.esprit.service.ServiceEvent;
import tn.esprit.service.ServicePlanning;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ServiceEvent se = new ServiceEvent();
        ServicePlanning sp = new ServicePlanning();

        try {

            //Event event = new Event(1, "Music Festival", "Biggest festival in town", "Tunis", "2025-03-10", 2, 150);
            List<Event> eventsSortedByPrice = se.sortBy("nom");

            for (Event e : eventsSortedByPrice) {
                System.out.println(e);
            }

           /* se.ajouter(event);
            System.out.println(" Event added!");

            // DISPLAY EVENTS
            System.out.println("\n Events in Database:");
            for (Event e : se.afficher()) {
                System.out.println(e);
            }*/


            /*Event updatedEvent = new Event(1, "Updated Festival", "Updated Description", "Updated Location", "2025-03-15", 3, 200);
            se.modifier(updatedEvent);
            System.out.println(" Event modified!");*/

            // ADD PLANNING
            /*List<Event> eventList = new ArrayList<>();
            eventList.add(new Event(2, "Art Exhibition", "Gallery showcase", "Sousse", "2025-04-15", 3, 200));*/

            //Planning planning = new Planning(1, "2025-03-01", "Spring Plan", eventList);
            /*sp.ajouter(planning);
            System.out.println(" Planning added!");*/

            // DISPLAY PLANNINGS
            /*System.out.println("\n Plannings in Database:");
            for (Planning p : sp.afficher()) {
                System.out.println(p);
            }
*/
            // MODIFY PLANNING

            //sp.modifier(new Planning(9,"2012-05-05", " wooho ", eventList)) ;
            //System.out.println(" Planning modified!");

            // DELETE EVENT
             /* se.supprimer(1);
              System.out.println("Event deleted!");*/

            // DELETE PLANNING (Uncomment to test)
               /*sp.supprimer(1);
               System.out.println(" Planning deleted!");*/

        } catch (SQLException e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }
}
