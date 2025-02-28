package tn.esprit.service;

import tn.esprit.utils.MyDatabase;
import tn.esprit.entities.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceEvent implements IService<Event> {

    private Connection connection;

    public ServiceEvent() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Event e) throws SQLException {
        String sql = "INSERT INTO `event`(`nom`, `description`, `lieu`, `date_event`, `duree`, `prix`) " +
                "VALUES ('" + e.getNom() + "', '" + e.getDescription() + "', '" + e.getLieu() + "', '" + e.getDateEvent() + "', " + e.getDuree() + ", " + e.getPrix() + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }


    @Override
    public void modifier(Event e) throws SQLException {
        String sql = "UPDATE `event` SET `nom`=?, `description`=?, `lieu`=?, `date_event`=?, `duree`=?, `prix`=? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, e.getNom());
        ps.setString(2, e.getDescription());
        ps.setString(3, e.getLieu());
        ps.setString(4, e.getDateEvent());
        ps.setInt(5, e.getDuree());
        ps.setFloat(6, e.getPrix());
        ps.setInt(7, e.getId());
        ps.executeUpdate();
    }


    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM `event` WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }


    @Override
    public List<Event> afficher() throws SQLException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM `event`";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            events.add(new Event(resultSet.getInt("id"), resultSet.getString("nom"), resultSet.getString("description"), resultSet.getString("lieu"), resultSet.getString("date_event"), resultSet.getInt("duree"), resultSet.getInt("prix")));

        }
        return events;
    }
















    //***cette methode permet la convertion du resultSet into a list of events
    private List<Event> convertResultSetToList(ResultSet resultSet) throws SQLException {
        List<Event> events = new ArrayList<>();
        while(resultSet.next()) {
            Event event = new Event(
                    resultSet.getInt("id"),
                    resultSet.getString("nom"),
                    resultSet.getString("description"),
                    resultSet.getString("lieu"),
                    resultSet.getString("date_event"),
                    resultSet.getInt("duree"),
                    resultSet.getFloat("prix")
            );

            events.add(event);
        }
        return events;
    }
    //////////////////


    public List<Event> getAll() throws SQLException {
        String sql = "SELECT * FROM `event`";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();

        return convertResultSetToList(resultSet).stream()
                .sorted(Comparator.comparing(Event::getNom))
                .toList();
    }


    public List<Event> getByName(String name) throws SQLException {
        return getAll().stream()
                .filter(Event -> Event.getNom().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }



    public List<Event> getByDate(String date) throws SQLException {
        return getAll().stream().filter(Event -> Event.getDateEvent().equals(date)).collect(Collectors.toList());
    }


    public List<Event> getByPrice(float minPrice, float maxPrice) throws SQLException {
        return getAll().stream()
                .filter(event -> event.getPrix() >= minPrice && event.getPrix() <= maxPrice)
                .collect(Collectors.toList());
    }


    public List<Event> getByLocation(String lieu ) throws SQLException {
        return getAll().stream()
                .filter(Event -> Event.getLieu().toLowerCase().contains(lieu.toLowerCase()))
                .collect(Collectors.toList());
    }


    @Override
    public List<Event> rechercher(String recherche) throws SQLException {
        String sql = "SELECT * FROM `event`";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();

        return convertResultSetToList(resultSet).stream()
                .filter(event -> event.getNom().toLowerCase().contains(recherche.toLowerCase()) ||
                        event.getDateEvent().toLowerCase().contains(recherche.toLowerCase()) ||
                        event.getLieu().toLowerCase().contains(recherche.toLowerCase()))
                .toList();
    }


    public List<Event> sortBy(String critere) throws SQLException {
        if (!critere.equalsIgnoreCase("nom") && !critere.equalsIgnoreCase("prix")) {
            throw new IllegalArgumentException("Invalid sorting criterion: " + critere);
        }


        String sql = "SELECT * FROM `event` ORDER BY " + critere + " ASC";

        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();

        return convertResultSetToList(resultSet);
    }




}
