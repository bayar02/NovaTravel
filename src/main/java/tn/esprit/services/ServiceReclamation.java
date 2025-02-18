package tn.esprit.services;

import tn.esprit.entities.User;
import tn.esprit.entities.reclamation;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReclamation implements IService <reclamation>{
    public static User user = new User(1,"feriel","feriel");
    private Connection connection;

    public ServiceReclamation() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(reclamation r, int id) throws SQLException {
        String sql = "INSERT INTO `reclamation`(`id_user`, `date_reclamation`, `type`, `message`) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setInt(1, id);
        ps.setDate(2, new Date(System.currentTimeMillis()));
        ps.setString(3, r.getType());
        ps.setString(4, r.getMessage());

        ps.executeUpdate();
    }



    @Override
    public void modifier(reclamation r) throws SQLException {
        String sql = "UPDATE `reclamation` SET `id_user`=?, `date_reclamation`=?, `type`=?, `message`=? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, user.getId());
        ps.setDate(2, r.getDate_reclamation());
        ps.setString(3, r.getType());
        ps.setString(4, r.getMessage());
        ps.setInt(5, r.getId());
        ps.executeUpdate();
    }
    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM `reclamation` WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<reclamation> afficher() throws SQLException {
        List<reclamation> reclamations = new ArrayList<>();
        String sql = "SELECT * FROM `reclamation`";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                reclamations.add(new reclamation(
                        resultSet.getInt("id"),
                        resultSet.getInt("id_user"),

                        resultSet.getDate("date_reclamation"),
                        resultSet.getString("type"),
                        resultSet.getString("message")
                ));
            }
        }
        return reclamations;
    }
    }






