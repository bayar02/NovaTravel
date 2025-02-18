package services;
import models.reservation_hebergement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.MyDb;

public class Servicereservation_hebergement implements Ireservation_hebergement<reservation_hebergement> {

    private Connection connection;

    public Servicereservation_hebergement() {
        connection = MyDb.getInstance().getConnection();
    }

    @Override
    public void create(reservation_hebergement r) throws SQLException {
        String sql = "INSERT INTO `reservation_hebergement`(`id_hebergement`, `date_debut`, `date_fin`, `nb_perso`) " +
                "VALUES (?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, r.getId_hebergement());
        ps.setDate(2, r.getDate_debut());
        ps.setDate(3, r.getDate_fin());
        ps.setInt(4, r.getNb_perso());
        ps.executeUpdate();
    }

    @Override
    public void update(reservation_hebergement r) throws SQLException {
        String sql = "UPDATE reservation_hebergement set `id_hebergement`=?, `date_debut`=?, `date_fin`=?, `nb_perso`=? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, r.getId_hebergement());
        ps.setDate(2, r.getDate_debut());
        ps.setDate(3, r.getDate_fin());
        ps.setInt(4, r.getNb_perso());
        ps.setInt(5, r.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM `reservation_hebergement` WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<reservation_hebergement> getAll() throws SQLException {
        List<reservation_hebergement> reservations = new ArrayList<>();
        String sql = "SELECT * FROM `reservation_hebergement`";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            reservations.add(new reservation_hebergement(
                    resultSet.getInt("id"),
                    resultSet.getInt("id_hebergement"),
                    resultSet.getDate("date_debut"),
                    resultSet.getDate("date_fin"),
                    resultSet.getInt("nb_perso")
            ));
        }

        return reservations;
    }
}

