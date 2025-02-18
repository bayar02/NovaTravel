package tn.esprit.services;

import tn.esprit.entities.reponse;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ServiceReponse implements IService <reponse>{


    private Connection connection;

    public ServiceReponse() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(reponse r,int id) throws SQLException {
        String sql = "INSERT INTO `reponse`(`id_reclamation`, `message`, `date_reponse`) " +
                "VALUES (" + id + ", '" + r.getMessage() + "', '" + r.getDate_reponse() + "')";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }
    @Override
    public void modifier(reponse r) throws SQLException {
        String sql = "UPDATE `reponse` SET `id_reclamation`=?, `message`=?, `date_reponse`=? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, r.getId_reclamation());
        ps.setString(2, r.getMessage());
        ps.setDate(3, r.getDate_reponse());
        ps.setInt(4, r.getId());
        ps.executeUpdate();
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM `reponse` WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<reponse> afficher() throws SQLException {
        List<reponse> reponses = new ArrayList<>();
        String sql = "SELECT * FROM `reponse`";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                reponses.add(new reponse(
                        resultSet.getInt("id"),
                        resultSet.getInt("id_reclamation"),
                        resultSet.getString("message"),
                        resultSet.getDate("date_reponse")
                ));
            }
        }
        return reponses;
    }
    public List<reponse> getReponsesByReclamationId(int reclamationId) throws SQLException {
        List<reponse> reponses = new ArrayList<>();
        String query = "SELECT * FROM reponse WHERE id_reclamation = ?";

             PreparedStatement pstmt = connection.prepareStatement(query) ;

            pstmt.setInt(1, reclamationId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                reponse reponse = new reponse(
                        rs.getInt("id"),
                        rs.getInt("id_reclamation"),
                        rs.getString("message"),
                        rs.getDate("date_reponse")
                );
                reponses.add(reponse);
            }

        return reponses;
    }





}
