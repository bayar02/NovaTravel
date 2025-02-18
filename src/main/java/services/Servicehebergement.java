package services;

import models.hebergement;
import utils.MyDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Servicehebergement implements Ihebergement <hebergement>{

    private Connection connection;

    public Servicehebergement() {
        connection = MyDb.getInstance().getConnection();
    }



    public List<String> getAllhebergementID() {
        List<String> hebid = new ArrayList<>();
        try {
            Connection conn =MyDb.getInstance().getConnection();
            String query = "SELECT id FROM hebergement"; // Adjust table/column names if necessary
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                hebid.add(rs.getString("id"));
            }
            System.out.println("Pack id from DB: " + hebid);

        } catch (SQLException e) {
            System.out.println("Error fetching pack names: " + e.getMessage());
        }
        return hebid;
    }

    @Override
    public void create(hebergement hebergement) throws SQLException {
        String sql = "INSERT INTO hebergement (nom, description, prix_nuit, type, adresse) VALUES (?, ?, ?, ?, ?)";

        if (hebergement.getNom() == null || hebergement.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'hébergement ne peut pas être null ou vide.");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, hebergement.getNom());
            ps.setString(2, hebergement.getDescription());
            ps.setFloat(3, hebergement.getPrix_nuit());
            ps.setString(4, hebergement.getType());
            ps.setString(5, hebergement.getAdresse());

            ps.executeUpdate();
        }
    }


    @Override
    public void update(hebergement h) throws SQLException {
        String sql = "UPDATE `hebergement` SET `type`=?, `nom`=?, `adresse`=?, `description`=?, `prix_nuit`=?, `photo`=? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, h.getType());
        ps.setString(2, h.getNom());
        ps.setString(3, h.getAdresse());
        ps.setString(4, h.getDescription());
        ps.setFloat(5, h.getPrix_nuit());
        ps.setString(6, h.getPhoto());
        ps.setInt(7, h.getId());
        ps.executeUpdate();
    }
    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM `hebergement` WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        return false;
    }
    @Override
    public List <hebergement> getAll() throws SQLException {
        List<hebergement> hebergements = new ArrayList<>();
        String sql = "SELECT * FROM `hebergement`";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            hebergements.add(new hebergement(
                    resultSet.getInt("id"),
                    resultSet.getString("type"),
                    resultSet.getString("nom"),
                    resultSet.getString("adresse"),
                    resultSet.getString("description"),
                    resultSet.getFloat("prix_nuit")
            ));
        }

        return hebergements;
    }



}
