package Services;

import Entities.Reclamation;
import Entities.Reponse;
import Utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReponseService implements IReponse<Reponse> {
    private Connection connection;

    public ReponseService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Reponse reponse) throws SQLException {
        String query = "INSERT INTO reponse (id_reclamation, message, date_reponse) VALUES (?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, reponse.getIdReclamation());
            pst.setString(2, reponse.getMessage());
            pst.setDate(3, new java.sql.Date(reponse.getDateReponse().getTime()));

            pst.executeUpdate();
            System.out.println("Response added successfully!");
        }
    }

    @Override
    public void modifier(Reponse reponse) throws SQLException {
        String query = "UPDATE reponse SET message=?, date_reponse=? WHERE id=?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, reponse.getMessage());
            pst.setDate(2, new java.sql.Date(reponse.getDateReponse().getTime())); // Fixed: Index should be 2
            pst.setInt(3, reponse.getId()); // Fixed: Index should be 3

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Response updated successfully!");
            } else {
                System.out.println("⚠ No response found with ID: " + reponse.getId());
            }
        }
    }


    @Override
    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM reponse WHERE id=?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);

            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Response deleted successfully!");
            } else {
                System.out.println("No response found with ID: " + id);
            }
        }
    }

    @Override
    public List<Reponse> afficher() {
        List<Reponse> reponseList = new ArrayList<>();
        String query = "SELECT * FROM reponse";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Reponse reponse = new Reponse();
                reponse.setId(resultSet.getInt("id"));
                reponse.setIdReclamation(resultSet.getInt("id_reclamation"));
                reponse.setMessage(resultSet.getString("message"));
                reponse.setDateReponse(resultSet.getDate("date_reponse"));
                reponse.setMessage(resultSet.getString("message"));
                reponseList.add(reponse);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving dechets: " + e.getMessage());
        }
        return reponseList;
    }

    public List<Reponse> searchReponses(String keyword) throws SQLException {
        List<Reponse> reponseList = new ArrayList<>();
        String query = "SELECT * FROM reponse WHERE message LIKE ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, "%" + keyword + "%");
            try (ResultSet resultSet = pst.executeQuery()) {
                while (resultSet.next()) {
                    Reponse reponse = new Reponse();
                    reponse.setId(resultSet.getInt("id"));
                    reponse.setIdReclamation(resultSet.getInt("id_reclamation"));
                    reponse.setMessage(resultSet.getString("message"));
                    reponse.setDateReponse(resultSet.getDate("date_reponse"));
                    reponseList.add(reponse);
                }
            }
        }
        return reponseList;
    }

}
