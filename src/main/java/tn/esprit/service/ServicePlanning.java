package tn.esprit.service;

import tn.esprit.entities.Event;
import tn.esprit.entities.Planning;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServicePlanning implements IService<Planning> {
    private Connection connection;

    public ServicePlanning() {
        connection = MyDatabase.getInstance().getConnection();
    }


    @Override
    public void ajouter(Planning p) throws SQLException {

        String sql = "INSERT INTO `planning` (`nom`, `date_creation`) VALUES ('"
                + p.getNom() + "', '" + p.getDateCreation() + "')";

        Statement statement = connection.createStatement();
        statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

        ResultSet rs = statement.getGeneratedKeys();
        int planningId = -1;
        if (rs.next()) {
            planningId = rs.getInt(1);
        }

        if (planningId != -1) {
            for (Event event : p.getEvents()) {
                String eventSql = "INSERT INTO `planning_events` (`id_planning`, `id_event`) VALUES ("
                        + planningId + ", " + event.getId() + ")";
                statement.executeUpdate(eventSql);
            }
        }
    }




    @Override
    public void modifier(Planning p) throws SQLException {

        String sql = "UPDATE planning SET nom = '" + p.getNom() + "', date_creation = '" + p.getDateCreation() + "' WHERE id = " + p.getId();
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);


        String deleteJoinSql = "DELETE FROM planning_events WHERE id_planning = " + p.getId();
        statement.executeUpdate(deleteJoinSql);


        for (Event event : p.getEvents()) {
            String joinSql = "INSERT INTO planning_events (id_planning, id_event) VALUES (" + p.getId() + ", " + event.getId() + ")";
            statement.executeUpdate(joinSql);
        }
    }


    @Override
    public void supprimer(int id) throws SQLException {

        String deleteJoinSql = "DELETE FROM planning_events WHERE id_planning = " + id;
        Statement statement = connection.createStatement();
        statement.executeUpdate(deleteJoinSql);


        String sql = "DELETE FROM planning WHERE id = " + id;
        statement.executeUpdate(sql);
    }


    @Override
    public List<Planning> afficher() throws SQLException {
        List<Planning> plannings = new ArrayList<>();
        String sql = "SELECT * FROM planning";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);


        ServiceEvent serviceEvent = new ServiceEvent();
        List<Event> allEvents = serviceEvent.afficher();

        while (resultSet.next()) {
            Planning planning = new Planning(
                    resultSet.getInt("id"),
                    resultSet.getString("nom"),
                    resultSet.getString("date_creation"),
                    new ArrayList<>()
            );


            String eventIdQuery = "SELECT id_event FROM planning_events WHERE id_planning = ?";
            PreparedStatement eventIdStmt = connection.prepareStatement(eventIdQuery);
            eventIdStmt.setInt(1, planning.getId());
            ResultSet eventIdResultSet = eventIdStmt.executeQuery();

            while (eventIdResultSet.next()) {
                int eventId = eventIdResultSet.getInt("id_event");


                for (Event event : allEvents) {
                    if (event.getId() == eventId) {
                        planning.getEvents().add(event);
                        break;
                    }
                }
            }

            plannings.add(planning);
        }
        return plannings;
    }

    public List<Planning> getAll() throws SQLException {
        List<Planning> plannings = new ArrayList<>();
        String sql = "SELECT * FROM planning";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        ServiceEvent serviceEvent = new ServiceEvent();
        List<Event> allEvents = serviceEvent.afficher();  // Assuming afficher() retrieves all events

        while (resultSet.next()) {
            Planning planning = new Planning(
                    resultSet.getInt("id"),
                    resultSet.getString("date_creation"),
                    resultSet.getString("nom"),
                    new ArrayList<>()
            );

            // Get associated events for the current planning
            String eventIdQuery = "SELECT id_event FROM planning_events WHERE id_planning = ?";
            PreparedStatement eventIdStmt = connection.prepareStatement(eventIdQuery);
            eventIdStmt.setInt(1, planning.getId());
            ResultSet eventIdResultSet = eventIdStmt.executeQuery();

            while (eventIdResultSet.next()) {
                int eventId = eventIdResultSet.getInt("id_event");

                for (Event event : allEvents) {
                    if (event.getId() == eventId) {
                        planning.getEvents().add(event);
                        break;
                    }
                }
            }

            plannings.add(planning);
        }

        return plannings;
    }

    public Planning getByName(String name) throws SQLException {
        String sql = "SELECT * FROM planning WHERE nom = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Planning planning = new Planning(
                            resultSet.getInt("id"),
                            resultSet.getString("date_creation"),
                            resultSet.getString("nom"),
                            new ArrayList<>()
                    );


                    String eventIdQuery = "SELECT id_event FROM planning_events WHERE id_planning = ?";
                    try (PreparedStatement eventIdStmt = connection.prepareStatement(eventIdQuery)) {
                        eventIdStmt.setInt(1, planning.getId());
                        try (ResultSet eventIdResultSet = eventIdStmt.executeQuery()) {
                            ServiceEvent serviceEvent = new ServiceEvent();
                            List<Event> allEvents = serviceEvent.afficher(); // Assuming afficher() retrieves all events


                            planning.setEvents(
                                    allEvents.stream()
                                            .filter(event -> {
                                                try {
                                                    return eventIdResultSet.next() && event.getId() == eventIdResultSet.getInt("id_event");
                                                } catch (SQLException e) {
                                                    throw new RuntimeException("Error processing event ID", e);
                                                }
                                            })
                                            .collect(Collectors.toList())
                            );
                        }
                    }

                    return planning;
                }
            }
        }
        return null;
    }

    public List<Planning> rechercher(String recherche) throws SQLException {
        List<Planning> plannings = new ArrayList<>();
        String sql = "SELECT * FROM planning WHERE nom LIKE ? OR date_creation LIKE ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        String searchTerm = "%" + recherche + "%";  // Add wildcards for LIKE search
        statement.setString(1, searchTerm);
        statement.setString(2, searchTerm);

        ResultSet resultSet = statement.executeQuery();

        ServiceEvent serviceEvent = new ServiceEvent();
        List<Event> allEvents = serviceEvent.afficher();

        while (resultSet.next()) {
            Planning planning = new Planning(
                    resultSet.getInt("id"),
                    resultSet.getString("date_creation"),
                    resultSet.getString("nom"),
                    new ArrayList<>()
            );

            // Get associated events for the current planning
            String eventIdQuery = "SELECT id_event FROM planning_events WHERE id_planning = ?";
            PreparedStatement eventIdStmt = connection.prepareStatement(eventIdQuery);
            eventIdStmt.setInt(1, planning.getId());
            ResultSet eventIdResultSet = eventIdStmt.executeQuery();

            while (eventIdResultSet.next()) {
                int eventId = eventIdResultSet.getInt("id_event");

                for (Event event : allEvents) {
                    if (event.getId() == eventId) {
                        planning.getEvents().add(event);
                        break;
                    }
                }
            }

            plannings.add(planning);
        }

        return plannings;
    }




}
